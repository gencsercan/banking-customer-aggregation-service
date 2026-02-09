package com.sercangenc.banking.service.impl;

import com.sercangenc.banking.client.CampaignStatsClient;
import com.sercangenc.banking.client.CoreBankingClient;
import com.sercangenc.banking.client.MarketingClient;
import com.sercangenc.banking.config.AppProps;
import com.sercangenc.banking.dto.*;
import com.sercangenc.banking.service.CustomerProfileService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final CoreBankingClient coreBankingClient;
    private final MarketingClient marketingClient;
    private final CampaignStatsClient campaignStatsClient;
    private final AppProps props;

    public CustomerProfileServiceImpl(CoreBankingClient coreBankingClient,
                                      MarketingClient marketingClient,
                                      CampaignStatsClient campaignStatsClient,
                                      AppProps props) {
        this.coreBankingClient = coreBankingClient;
        this.marketingClient = marketingClient;
        this.campaignStatsClient = campaignStatsClient;
        this.props = props;
    }

    @Override
    public Mono<CustomerProfileResponse> getCustomerProfile(String customerId) {
        var timeouts = props.getTimeouts();
        var warnings = new HashMap<String, String>();

        Mono<TimedResult<CoreBankingInfo>> coreMono = timed("core-banking", coreBankingClient.fetchCoreBanking(customerId))
                .timeout(Duration.ofMillis(timeouts.getCoreBankingMs()))
                .onErrorResume(ex -> Mono.just(TimedResult.fail("core-banking", ex)));

        Mono<TimedResult<MarketingInfo>> marketingMono = timed("marketing", marketingClient.fetchMarketing(customerId))
                .timeout(Duration.ofMillis(timeouts.getMarketingMs()))
                .onErrorResume(ex -> Mono.just(TimedResult.fail("marketing", ex)));

        Mono<TimedResult<CampaignStats>> statsMono = timed("campaign-stats", campaignStatsClient.fetchStats(customerId))
                .timeout(Duration.ofMillis(timeouts.getCampaignStatsMs()))
                .onErrorResume(ex -> Mono.just(TimedResult.fail("campaign-stats", ex)));

        // Parallel aggregation, but resilient: partial data allowed
        return Mono.zip(coreMono, marketingMono, statsMono)
                .map(tuple -> {
                    var core = tuple.getT1();
                    var mkt = tuple.getT2();
                    var stats = tuple.getT3();

                    List<SourceStatus> statuses = List.of(
                            core.toStatus(),
                            mkt.toStatus(),
                            stats.toStatus()
                    );

                    if (!core.ok()) warnings.put("core-banking", core.errorMessage());
                    if (!mkt.ok()) warnings.put("marketing", mkt.errorMessage());
                    if (!stats.ok()) warnings.put("campaign-stats", stats.errorMessage());

                    return new CustomerProfileResponse(
                            customerId,
                            Instant.now(),
                            core.value(),
                            mkt.value(),
                            stats.value(),
                            statuses,
                            warnings.isEmpty() ? null : warnings
                    );
                });
    }

    private static <T> Mono<TimedResult<T>> timed(String source, Mono<T> mono) {
        long start = System.currentTimeMillis();
        return mono
                .map(v -> TimedResult.ok(source, v, System.currentTimeMillis() - start))
                .switchIfEmpty(Mono.fromSupplier(() -> TimedResult.ok(source, null, System.currentTimeMillis() - start)));
    }

    public record TimedResult<T>(String source, boolean ok, T value, long latencyMs, String errorCode, String errorMessage) {
        static <T> TimedResult<T> ok(String source, T value, long latencyMs) {
            return new TimedResult<>(source, true, value, latencyMs, null, null);
        }
        static <T> TimedResult<T> fail(String source, Throwable ex) {
            String msg = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
            String code = msg.length() > 80 ? msg.substring(0, 80) : msg;
            return new TimedResult<>(source, false, null, 0, code, msg);
        }
        SourceStatus toStatus() {
            return ok
                    ? SourceStatus.ok(source, latencyMs)
                    : SourceStatus.fail(source, latencyMs, errorCode, errorMessage);
        }
    }
}
