package com.sercangenc.banking.client.mock;

import com.sercangenc.banking.client.CoreBankingClient;
import com.sercangenc.banking.config.AppProps;
import com.sercangenc.banking.dto.CoreBankingInfo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class MockCoreBankingClient implements CoreBankingClient {

    private final AppProps props;

    public MockCoreBankingClient(AppProps props) {
        this.props = props;
    }

    @Override
    public Mono<CoreBankingInfo> fetchCoreBanking(String customerId) {
        var sim = props.getSimulate();
        Mono<CoreBankingInfo> base = sim.isCoreBankingFail()
                ? Mono.error(new RuntimeException("CORE_BANKING_DOWN"))
                : Mono.just(new CoreBankingInfo("SME", "0459", "ACTIVE"));

        return base.delayElement(Duration.ofMillis(sim.getCoreBankingDelayMs()));
    }
}
