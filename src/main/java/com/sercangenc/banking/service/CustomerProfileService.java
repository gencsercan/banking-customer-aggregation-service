package com.sercangenc.banking.service;

import com.sercangenc.banking.dto.CustomerProfileResponse;
import reactor.core.publisher.Mono;

public interface CustomerProfileService {
    Mono<CustomerProfileResponse> getCustomerProfile(String customerId);
}
