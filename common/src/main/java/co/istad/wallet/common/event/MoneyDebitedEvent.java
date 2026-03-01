package co.istad.wallet.common.event;

import co.istad.wallet.common.vo.*;

import java.time.Instant;
import java.time.LocalDate;

public record MoneyDebitedEvent(
        TransactionId transactionId,
        WalletId walletId,
        TransferId transferId,
        WalletId toWalletId,
        Money amount,
        Money balance,
        LocalDate occurredDate,
        Money withdrawnToday,
        Instant timestamp
) {
}
