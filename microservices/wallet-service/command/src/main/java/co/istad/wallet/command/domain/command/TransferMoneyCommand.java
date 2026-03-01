package co.istad.wallet.command.domain.command;

import co.istad.wallet.common.vo.TransactionId;
import co.istad.wallet.common.vo.TransferId;
import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.common.vo.Money;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record TransferMoneyCommand(
        @TargetAggregateIdentifier
        WalletId walletId,
        TransactionId transactionId,
        TransferId transferId,
        Money amount,
        WalletId toWalletId
) {
}
