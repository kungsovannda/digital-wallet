package co.istad.wallet.common.event;

import co.istad.wallet.common.vo.*;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record MoneyWithdrawnEvent(
        TransactionId transactionId,
        WalletId walletId,
        Money amount,
        Money balance,
        LocalDate occurredDate,
        Money withdrawnToday,
        Instant timestamp

) {
}
