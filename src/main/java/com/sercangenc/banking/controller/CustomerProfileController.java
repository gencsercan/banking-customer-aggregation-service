package com.sercangenc.banking.controller;

import com.sercangenc.banking.dto.CustomerProfileResponse;
import com.sercangenc.banking.service.CustomerProfileService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/customers")
public class CustomerProfileController {

    private final CustomerProfileService service;

    public CustomerProfileController(CustomerProfileService service) {
        this.service = service;
    }

    @GetMapping("/{customerId}/profile")
    public Mono<CustomerProfileResponse> getProfile(@PathVariable @NotBlank String customerId) {
        return service.getCustomerProfile(customerId);
    }
}
