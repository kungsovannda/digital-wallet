package co.istad.wallet.common.vo;

import java.util.UUID;

public record WalletId(
        UUID id
) {

    @Override
    public String toString() {
        return id.toString();
    }
}
