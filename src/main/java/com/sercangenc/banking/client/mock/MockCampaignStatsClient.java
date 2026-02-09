package com.sercangenc.banking.client.mock;

import com.sercangenc.banking.client.CampaignStatsClient;
import com.sercangenc.banking.config.AppProps;
import com.sercangenc.banking.dto.CampaignStats;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class MockCampaignStatsClient implements CampaignStatsClient {

    private final AppProps props;

    public MockCampaignStatsClient(AppProps props) {
        this.props = props;
    }

    @Override
    public Mono<CampaignStats> fetchStats(String customerId) {
        var sim = props.getSimulate();
        Mono<CampaignStats> base = sim.isCampaignStatsFail()
                ? Mono.error(new RuntimeException("STATS_SERVICE_DOWN"))
                : Mono.just(new CampaignStats(14, 63, 0.087));

        return base.delayElement(Duration.ofMillis(sim.getCampaignStatsDelayMs()));
    }
}
