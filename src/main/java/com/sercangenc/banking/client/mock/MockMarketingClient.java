package com.sercangenc.banking.client.mock;

import com.sercangenc.banking.client.MarketingClient;
import com.sercangenc.banking.config.AppProps;
import com.sercangenc.banking.dto.MarketingInfo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class MockMarketingClient implements MarketingClient {

    private final AppProps props;

    public MockMarketingClient(AppProps props) {
        this.props = props;
    }

    @Override
    public Mono<MarketingInfo> fetchMarketing(String customerId) {
        var sim = props.getSimulate();
        Mono<MarketingInfo> base = sim.isMarketingFail()
                ? Mono.error(new RuntimeException("MARKETING_TIMEOUT"))
                : Mono.just(new MarketingInfo(true, List.of("SMS", "EMAIL", "PUSH"), "SME_WELCOME"));

        return base.delayElement(Duration.ofMillis(sim.getMarketingDelayMs()));
    }
}
