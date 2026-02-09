package com.sercangenc.banking.dto;

public record CampaignStats(
        int offersLast90Days,
        int clicksLast90Days,
        double conversionRate
) {}
