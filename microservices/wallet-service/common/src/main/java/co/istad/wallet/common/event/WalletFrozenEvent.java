package co.istad.wallet.common.event;

import co.istad.wallet.common.vo.*;
import java.time.LocalDate;

public record WalletFrozenEvent(
        WalletId walletId,
        LocalDate occurredDate
) {
}
