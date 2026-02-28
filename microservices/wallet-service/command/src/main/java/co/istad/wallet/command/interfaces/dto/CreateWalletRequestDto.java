package co.istad.wallet.command.interfaces.dto;

import co.istad.wallet.common.vo.Money;
import co.istad.wallet.common.vo.UserId;
import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.common.vo.WalletType;

public record CreateWalletRequestDto(
        UserId ownerId,
        Money balance,
        WalletType type) {
}
