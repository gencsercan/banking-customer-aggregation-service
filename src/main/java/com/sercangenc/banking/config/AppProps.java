package com.sercangenc.banking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProps {
    private Timeouts timeouts = new Timeouts();
    private Simulate simulate = new Simulate();

    public Timeouts getTimeouts() { return timeouts; }
    public void setTimeouts(Timeouts timeouts) { this.timeouts = timeouts; }
    public Simulate getSimulate() { return simulate; }
    public void setSimulate(Simulate simulate) { this.simulate = simulate; }

    public static class Timeouts {
        private long coreBankingMs = 800;
        private long marketingMs = 800;
        private long campaignStatsMs = 800;

        public long getCoreBankingMs() { return coreBankingMs; }
        public void setCoreBankingMs(long v) { this.coreBankingMs = v; }
        public long getMarketingMs() { return marketingMs; }
        public void setMarketingMs(long v) { this.marketingMs = v; }
        public long getCampaignStatsMs() { return campaignStatsMs; }
        public void setCampaignStatsMs(long v) { this.campaignStatsMs = v; }
    }

    public static class Simulate {
        private long coreBankingDelayMs = 120;
        private long marketingDelayMs = 250;
        private long campaignStatsDelayMs = 900;
        private boolean coreBankingFail = false;
        private boolean marketingFail = false;
        private boolean campaignStatsFail = false;

        public long getCoreBankingDelayMs() { return coreBankingDelayMs; }
        public void setCoreBankingDelayMs(long v) { this.coreBankingDelayMs = v; }
        public long getMarketingDelayMs() { return marketingDelayMs; }
        public void setMarketingDelayMs(long v) { this.marketingDelayMs = v; }
        public long getCampaignStatsDelayMs() { return campaignStatsDelayMs; }
        public void setCampaignStatsDelayMs(long v) { this.campaignStatsDelayMs = v; }
        public boolean isCoreBankingFail() { return coreBankingFail; }
        public void setCoreBankingFail(boolean v) { this.coreBankingFail = v; }
        public boolean isMarketingFail() { return marketingFail; }
        public void setMarketingFail(boolean v) { this.marketingFail = v; }
        public boolean isCampaignStatsFail() { return campaignStatsFail; }
        public void setCampaignStatsFail(boolean v) { this.campaignStatsFail = v; }
    }
}
