package co.istad.wallet.command.interfaces.dto;

import co.istad.wallet.common.vo.Money;
import co.istad.wallet.common.vo.WalletId;
import jakarta.validation.constraints.NotNull;

public record DepositMoneyRequestDto(
        @NotNull(message = "Amount is required")
        Money amount
) {
}
