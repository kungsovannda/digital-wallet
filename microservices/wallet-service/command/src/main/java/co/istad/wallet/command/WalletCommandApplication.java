package co.istad.wallet.command;

import co.istad.wallet.command.domain.command.CreateWalletCommand;
import co.istad.wallet.command.domain.command.DepositMoneyCommand;
import co.istad.wallet.common.vo.*;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class WalletCommandApplication {

    private final CommandGateway commandGateway;

    public static void main(String[] args) {
        SpringApplication.run(WalletCommandApplication.class, args);
    }

}
