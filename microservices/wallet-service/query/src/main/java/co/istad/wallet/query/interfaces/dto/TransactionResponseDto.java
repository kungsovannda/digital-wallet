package co.istad.wallet.query.interfaces.dto;

import co.istad.wallet.common.vo.Money;
import co.istad.wallet.query.vo.TransactionType;

import java.time.Instant;

public record TransactionResponseDto(
        String transactionId,
        String walletId,
        Money amount,
        TransactionType type,
        String transferId,
        String counterpartWalletId,
        Instant timestamp
) {
}
