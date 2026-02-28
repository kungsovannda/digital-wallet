package co.istad.wallet.query.view;

import co.istad.wallet.common.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Document("wallets")
@AllArgsConstructor
@NoArgsConstructor
public class WalletView {

    @Id
    private String walletId;
    private String ownerId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal balance;
    private String currency;
    private WalletType type;
    private WalletStatus status;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal withdrawnToday;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal dailyWithdrawLimit;
    private LocalDate lastWithdrawalDate;
    private Instant createdAt;
    private Instant updatedAt;

}
