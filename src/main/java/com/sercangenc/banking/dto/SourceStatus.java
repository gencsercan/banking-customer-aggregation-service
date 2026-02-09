package com.sercangenc.banking.dto;

public record SourceStatus(
        String source,
        boolean ok,
        long latencyMs,
        String errorCode,
        String message
) {
    public static SourceStatus ok(String source, long latencyMs) {
        return new SourceStatus(source, true, latencyMs, null, null);
    }
    public static SourceStatus fail(String source, long latencyMs, String errorCode, String message) {
        return new SourceStatus(source, false, latencyMs, errorCode, message);
    }
}
