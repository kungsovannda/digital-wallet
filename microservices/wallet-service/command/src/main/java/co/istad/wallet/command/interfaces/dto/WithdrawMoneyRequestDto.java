package co.istad.wallet.command.interfaces.dto;

import co.istad.wallet.common.vo.Money;
import jakarta.validation.constraints.NotNull;

public record WithdrawMoneyRequestDto(
        @NotNull(message = "Amount is required")
        Money amount
) {
}
