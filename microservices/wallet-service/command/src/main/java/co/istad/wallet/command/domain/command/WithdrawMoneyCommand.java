package co.istad.wallet.command.domain.command;

import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.common.vo.Money;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record WithdrawMoneyCommand(
        @TargetAggregateIdentifier
        WalletId walletId,
        Money amount
) {
}
