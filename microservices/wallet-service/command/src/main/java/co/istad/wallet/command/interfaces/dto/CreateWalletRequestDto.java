package co.istad.wallet.command.interfaces.dto;

import co.istad.wallet.common.vo.Money;
import co.istad.wallet.common.vo.UserId;
import co.istad.wallet.common.vo.WalletType;
import jakarta.validation.constraints.NotNull;

public record CreateWalletRequestDto(
        @NotNull(message = "Balance is required")
        Money balance,
        @NotNull(message = "Wallet type is required")
        WalletType type) {
}
