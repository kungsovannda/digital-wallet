package co.istad.wallet.query.view;

import co.istad.wallet.query.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionView {
    @Id
    private String transactionId;
    private String walletId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;
    private String currency;
    private TransactionType type;
    private String transferId;
    private String counterpartWalletId;
    private Instant timestamp;
}
