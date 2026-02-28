package co.istad.wallet.common.event;

import co.istad.wallet.common.vo.*;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record MoneyDepositedEvent(
        TransactionId transactionId,
        WalletId walletId,
        Money amount,
        Money balance,
        LocalDate occurredDate,
        Instant timestamp
) {
}
