package co.istad.wallet.command.interfaces.dto;

import co.istad.wallet.common.vo.Money;
import co.istad.wallet.common.vo.WalletId;

public record WithdrawMoneyRequestDto(
        Money amount
) {
}
