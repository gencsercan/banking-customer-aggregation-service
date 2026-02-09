package com.sercangenc.banking.dto;

import java.util.List;

public record MarketingInfo(
        boolean contactable,
        List<String> channels,
        String lastCampaign
) {}
