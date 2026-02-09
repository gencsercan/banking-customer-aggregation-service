package com.sercangenc.banking.client;

import com.sercangenc.banking.dto.CoreBankingInfo;
import reactor.core.publisher.Mono;

public interface CoreBankingClient {
    Mono<CoreBankingInfo> fetchCoreBanking(String customerId);
}
