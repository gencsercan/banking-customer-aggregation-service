package com.sercangenc.banking.dto;

public record CoreBankingInfo(
        String customerSegment,
        String branchCode,
        String status
) {}
