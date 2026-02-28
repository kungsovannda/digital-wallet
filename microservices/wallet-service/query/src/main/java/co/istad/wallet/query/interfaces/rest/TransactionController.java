package co.istad.wallet.query.interfaces.rest;


import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.query.interfaces.dto.TransactionResponseDto;
import co.istad.wallet.query.interfaces.dto.WalletResponseDto;
import co.istad.wallet.query.query.GetTransactionHistoryQuery;
import co.istad.wallet.query.query.GetWalletQuery;
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
    public List<TransactionResponseDto> getTransaction(@PathVariable UUID walletId){
        GetTransactionHistoryQuery getWalletQuery = new GetTransactionHistoryQuery(new WalletId(walletId));
        return queryGateway.query(getWalletQuery, ResponseTypes.multipleInstancesOf(TransactionResponseDto.class)).join();

    }
}
