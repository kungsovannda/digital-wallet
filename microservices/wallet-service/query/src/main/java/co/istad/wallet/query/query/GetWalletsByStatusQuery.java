package co.istad.wallet.query.query;

import co.istad.wallet.common.vo.WalletStatus;

public record GetWalletsByStatusQuery(
        WalletStatus status) {
}
