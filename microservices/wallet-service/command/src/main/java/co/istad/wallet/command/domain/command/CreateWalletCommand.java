package co.istad.wallet.command.domain.command;

import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.common.vo.UserId;
import co.istad.wallet.common.vo.Money;
import co.istad.wallet.common.vo.WalletType;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
public record CreateWalletCommand(
        @TargetAggregateIdentifier
        WalletId walletId,
        UserId ownerId,
        Money balance,
        WalletType type
) {
}
