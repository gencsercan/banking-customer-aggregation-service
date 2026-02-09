package com.sercangenc.banking.client;

import com.sercangenc.banking.dto.MarketingInfo;
import reactor.core.publisher.Mono;

public interface MarketingClient {
    Mono<MarketingInfo> fetchMarketing(String customerId);
}
