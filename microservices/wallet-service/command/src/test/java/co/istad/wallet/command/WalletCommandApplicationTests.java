package co.istad.wallet.command;

import co.istad.wallet.command.domain.command.CreateWalletCommand;
import co.istad.wallet.command.domain.command.DepositMoneyCommand;
import co.istad.wallet.command.domain.command.TransferMoneyCommand;
import co.istad.wallet.command.domain.command.WithdrawMoneyCommand;
import co.istad.wallet.common.vo.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
class WalletCommandApplicationTests {

    @Autowired
    private CommandGateway commandGateway;

    private final UUID uuid = UUID.fromString("7eadca8e-3e38-42dc-b3f6-42ccbb56ab11");

    @Test
    void contextLoads() {
        commandGateway.send(new CreateWalletCommand(
                new WalletId(uuid),
                new UserId(UUID.randomUUID()),
                new Money(new BigDecimal("5000"), MoneyCurrency.KHR),
                WalletType.STANDARD
        ));
    }

    @Test
    void testDeposit(){
        commandGateway.send(new DepositMoneyCommand(
                new WalletId(uuid),
                new Money(new BigDecimal("1000"), MoneyCurrency.USD)
        ));
    }

    @Test
    void testWithdraw(){
        commandGateway.send(new WithdrawMoneyCommand(
                new WalletId(uuid),
                new Money(new BigDecimal("500"), MoneyCurrency.USD)
        ));
    }

    @Test
    void testTransfer(){
        commandGateway.send(new TransferMoneyCommand(
                    new WalletId(uuid),
                    new Money(new BigDecimal("100"), MoneyCurrency.USD),
                    new WalletId(UUID.fromString("71ebe1e1-5154-498d-986a-1cab526fab3b"))
                )
        );
    }

}
