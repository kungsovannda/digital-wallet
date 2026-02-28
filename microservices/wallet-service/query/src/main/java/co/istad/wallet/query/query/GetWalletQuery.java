package co.istad.wallet.query.query;

import co.istad.wallet.common.vo.WalletId;

public record GetWalletQuery(
        WalletId walletId
) {
}
