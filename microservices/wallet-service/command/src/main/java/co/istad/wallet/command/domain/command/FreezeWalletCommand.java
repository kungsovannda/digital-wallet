package co.istad.wallet.command.domain.command;

import co.istad.wallet.common.vo.WalletId;

public record FreezeWalletCommand(
        WalletId walletId
) {
}
