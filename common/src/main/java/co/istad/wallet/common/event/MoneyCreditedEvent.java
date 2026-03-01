package co.istad.wallet.common.event;

import co.istad.wallet.common.vo.*;

import java.time.Instant;
import java.time.LocalDate;

public record MoneyCreditedEvent(
        TransactionId transactionId,
        WalletId walletId,
        WalletId fromWalletId,
        TransferId transferId,
        Money amount,
        Money balance,
        LocalDate occurredDate,
        Instant timestamp
) {
}
