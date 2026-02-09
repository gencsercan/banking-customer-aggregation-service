package com.sercangenc.banking;

import com.sercangenc.banking.config.AppProps;
import com.sercangenc.banking.service.CustomerProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class CustomerProfileServiceTest {

    @Autowired CustomerProfileService service;
    @Autowired AppProps props;

    @Test
    void shouldReturnPartialWhenStatsTimeout() {
        props.getSimulate().setCampaignStatsDelayMs(2000); // force timeout
        StepVerifier.create(service.getCustomerProfile("123"))
                .assertNext(resp -> {
                    // core + marketing should be present, stats may be null
                    assert resp.customerId().equals("123");
                    assert resp.coreBanking() != null;
                    assert resp.marketing() != null;
                    // stats can be null due to timeout
                })
                .verifyComplete();
    }
}
