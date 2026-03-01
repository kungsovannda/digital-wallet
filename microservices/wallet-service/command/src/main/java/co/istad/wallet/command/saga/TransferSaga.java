package co.istad.wallet.command.saga;

import co.istad.wallet.command.domain.command.CreditMoneyCommand;
import co.istad.wallet.common.event.MoneyDebitedEvent;
import co.istad.wallet.common.vo.TransactionId;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@NoArgsConstructor
public class TransferSaga {

    @StartSaga
    @SagaEventHandler(associationProperty = "transferId")
    public void on(MoneyDebitedEvent event, @Autowired CommandGateway commandGateway) {
        commandGateway.send(
                new CreditMoneyCommand(
                        event.toWalletId(),
                        event.walletId(),
                        new TransactionId(UUID.randomUUID()),
                        event.transferId(),
                        event.amount()
                )
        );
    }


}
