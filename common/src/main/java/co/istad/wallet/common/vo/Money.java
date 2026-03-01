package co.istad.wallet.common.vo;

import java.math.BigDecimal;

public record Money(
        BigDecimal balance,
        MoneyCurrency currency
) {
}
