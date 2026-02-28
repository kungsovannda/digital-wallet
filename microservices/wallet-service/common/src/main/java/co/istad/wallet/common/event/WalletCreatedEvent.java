package co.istad.wallet.common.event;

import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.common.vo.UserId;
import co.istad.wallet.common.vo.Money;
import co.istad.wallet.common.vo.WalletStatus;
import co.istad.wallet.common.vo.WalletType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record WalletCreatedEvent(
        WalletId walletId,
        UserId ownerId,
        Money balance,
        WalletStatus status,
        WalletType type,
        Money dailyWithdrawalLimit,
        Instant createdAt
) {
}
