package com.sercangenc.banking.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record CustomerProfileResponse(
        String customerId,
        Instant generatedAt,
        CoreBankingInfo coreBanking,
        MarketingInfo marketing,
        CampaignStats campaignStats,
        List<SourceStatus> sourceStatuses,
        Map<String, String> warnings
) {}
