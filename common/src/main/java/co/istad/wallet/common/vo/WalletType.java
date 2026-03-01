package co.istad.wallet.common.vo;

import java.math.BigDecimal;

public enum WalletType {
    STANDARD(new BigDecimal("1000")),
    PREMIUM(new BigDecimal("5000")),
    BUSINESS(new BigDecimal("10000"));

    private final BigDecimal dailyLimit;

    WalletType(BigDecimal dailyLimit) { this.dailyLimit = dailyLimit; }

    public BigDecimal getDailyLimit() { return dailyLimit; }
}
