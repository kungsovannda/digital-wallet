package co.istad.wallet.query.interfaces.dto;

import co.istad.wallet.common.vo.WalletStatus;
import co.istad.wallet.common.vo.WalletType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record WalletResponseDto(
        String walletId,
        String ownerId,
        BigDecimal balance,
        String currency,
        WalletType type,
        WalletStatus status,
        BigDecimal withdrawnToday,
        BigDecimal dailyWithdrawLimit,
        LocalDate lastWithdrawalDate,
        Instant createdAt,
        Instant updatedAt
) {
}
