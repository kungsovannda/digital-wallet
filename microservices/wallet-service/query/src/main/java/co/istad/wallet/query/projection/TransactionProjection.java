package co.istad.wallet.query.projection;

import co.istad.wallet.common.event.MoneyCreditedEvent;
import co.istad.wallet.common.event.MoneyDebitedEvent;
import co.istad.wallet.common.event.MoneyDepositedEvent;
import co.istad.wallet.common.event.MoneyWithdrawnEvent;
import co.istad.wallet.query.interfaces.dto.TransactionResponseDto;
import co.istad.wallet.query.query.GetTransactionHistoryQuery;
import co.istad.wallet.query.repository.TransactionViewRepository;
import co.istad.wallet.query.view.TransactionView;
import co.istad.wallet.query.vo.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@ProcessingGroup("transaction-projection")
@RequiredArgsConstructor
public class TransactionProjection {

    private final TransactionViewRepository transactionViewRepository;

    @QueryHandler
    public List<TransactionResponseDto> handle(GetTransactionHistoryQuery query){
        List<TransactionView> transactionViews = transactionViewRepository.findAllByWalletId(query.walletId().toString());
        return transactionViews.stream().map(
                transactionView -> new TransactionResponseDto(
                        transactionView.getTransactionId(),
                        transactionView.getWalletId(),
                        transactionView.getAmount(),
                        transactionView.getCurrency(),
                        transactionView.getType(),
                        transactionView.getTransferId(),
                        transactionView.getCounterpartWalletId(),
                        transactionView.getTimestamp()
                )
        ).toList();
    }

    @EventHandler
    public void on(MoneyDepositedEvent event){
        TransactionView transactionView = new TransactionView(
                event.transactionId().id().toString(),
                event.walletId().id().toString(),
                event.amount().balance(),
                event.amount().currency().toString(),
                TransactionType.DEPOSIT,
                null,
                null,
                event.timestamp()
        );

        transactionViewRepository.save(transactionView);
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event){
        TransactionView transactionView = new TransactionView(
                event.transactionId().id().toString(),
                event.walletId().id().toString(),
                event.amount().balance(),
                event.amount().currency().toString(),
                TransactionType.WITHDRAW,
                null,
                null,
                event.timestamp()
        );

        transactionViewRepository.save(transactionView);
    }

    @EventHandler
    public void on(MoneyDebitedEvent event){
        TransactionView transactionView = new TransactionView(
                event.transactionId().id().toString(),
                event.walletId().id().toString(),
                event.amount().balance(),
                event.amount().currency().toString(),
                TransactionType.DEBIT,
                event.transferId().toString(),
                event.toWalletId().toString(),
                event.timestamp()
        );

        transactionViewRepository.save(transactionView);
    }

    @EventHandler
    public void on(MoneyCreditedEvent event){
        TransactionView transactionView = new TransactionView(
                event.transactionId().id().toString(),
                event.walletId().id().toString(),
                event.amount().balance(),
                event.amount().currency().toString(),
                TransactionType.CREDIT,
                event.transferId().toString(),
                event.fromWalletId().id().toString(),
                event.timestamp()
        );

        transactionViewRepository.save(transactionView);
    }

}
