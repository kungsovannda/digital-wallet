package co.istad.wallet.command.domain.command;

import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.common.vo.TransferId;
import co.istad.wallet.common.vo.Money;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreditMoneyCommand(
        @TargetAggregateIdentifier
        WalletId walletId,
        WalletId fromWalletId,
        TransferId transferId,
        Money amount
) {
}
