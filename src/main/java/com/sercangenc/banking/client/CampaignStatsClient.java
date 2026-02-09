package com.sercangenc.banking.client;

import com.sercangenc.banking.dto.CampaignStats;
import reactor.core.publisher.Mono;

public interface CampaignStatsClient {
    Mono<CampaignStats> fetchStats(String customerId);
}
