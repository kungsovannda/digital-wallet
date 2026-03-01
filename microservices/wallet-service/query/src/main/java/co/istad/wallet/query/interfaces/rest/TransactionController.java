package co.istad.wallet.query.interfaces.rest;

import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.query.interfaces.dto.TransactionResponseDto;
import co.istad.wallet.query.query.GetTransactionByIdQuery;
import co.istad.wallet.query.query.GetTransactionHistoryQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class TransactionController {

    private final QueryGateway queryGateway;

    @GetMapping("/{walletId}/transactions")
    public List<TransactionResponseDto> getTransactionHistory(@PathVariable UUID walletId) {
        GetTransactionHistoryQuery getTransactionHistoryQuery = new GetTransactionHistoryQuery(new WalletId(walletId));
        return queryGateway
                .query(getTransactionHistoryQuery, ResponseTypes.multipleInstancesOf(TransactionResponseDto.class))
                .join();
    }

    @GetMapping("/transactions/{transactionId}")
    public TransactionResponseDto getTransactionById(@PathVariable String transactionId) {
        GetTransactionByIdQuery getTransactionByIdQuery = new GetTransactionByIdQuery(transactionId);
        return queryGateway.query(getTransactionByIdQuery, ResponseTypes.instanceOf(TransactionResponseDto.class))
                .join();
    }
}
