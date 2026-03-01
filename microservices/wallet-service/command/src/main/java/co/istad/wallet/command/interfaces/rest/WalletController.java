package co.istad.wallet.command.interfaces.rest;


import co.istad.wallet.command.domain.command.CreateWalletCommand;
import co.istad.wallet.command.domain.command.DepositMoneyCommand;
import co.istad.wallet.command.domain.command.TransferMoneyCommand;
import co.istad.wallet.command.domain.command.WithdrawMoneyCommand;
import co.istad.wallet.command.interfaces.dto.CreateWalletRequestDto;
import co.istad.wallet.command.interfaces.dto.DepositMoneyRequestDto;
import co.istad.wallet.command.interfaces.dto.TransferMoneyRequestDto;
import co.istad.wallet.command.interfaces.dto.WithdrawMoneyRequestDto;
import co.istad.wallet.common.vo.TransactionId;
import co.istad.wallet.common.vo.TransferId;
import co.istad.wallet.common.vo.UserId;
import co.istad.wallet.common.vo.WalletId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final CommandGateway commandGateway;

    @PostMapping
    public ResponseEntity<?> createWallet(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateWalletRequestDto createWalletRequestDto){
        log.info("Create wallet user: {}", jwt.getClaims());
        commandGateway.sendAndWait(
                new CreateWalletCommand(
                        new WalletId(UUID.randomUUID()),
                        extractUserId(jwt),
                        createWalletRequestDto.balance(),
                        createWalletRequestDto.type()
                )
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{walletId}/deposit")
    public ResponseEntity<?> depositMoney(@PathVariable UUID walletId,@Valid @RequestBody DepositMoneyRequestDto depositMoneyRequestDto){
        commandGateway.sendAndWait(
                new DepositMoneyCommand(
                        new WalletId(walletId),
                        new TransactionId(UUID.randomUUID()),
                        depositMoneyRequestDto.amount()
                )
        );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/{walletId}/withdraw")
    public ResponseEntity<?> withdrawMoney(@PathVariable UUID walletId,@Valid @RequestBody WithdrawMoneyRequestDto withdrawMoneyRequestDto){
        commandGateway.sendAndWait(
                new WithdrawMoneyCommand(
                        new WalletId(walletId),
                        new TransactionId(UUID.randomUUID()),
                        withdrawMoneyRequestDto.amount()
                )
        );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<?> transferMoney(@PathVariable UUID walletId,@Valid @RequestBody TransferMoneyRequestDto transferMoneyRequestDto){
        commandGateway.sendAndWait(
                new TransferMoneyCommand(
                        new WalletId(walletId),
                        new TransactionId(UUID.randomUUID()),
                        new TransferId(UUID.randomUUID()),
                        transferMoneyRequestDto.amount(),
                        transferMoneyRequestDto.toWalletId()
                )
        );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private UserId extractUserId(Jwt jwt){
        return new UserId(
                UUID.fromString(jwt.getClaims().get("uuid").toString())
        );
    }
}
