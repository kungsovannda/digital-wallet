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
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final CommandGateway commandGateway;

    @PostMapping
    public ResponseEntity<?> createWallet(@Valid @RequestBody CreateWalletRequestDto createWalletRequestDto){
        commandGateway.sendAndWait(
                new CreateWalletCommand(
                        new WalletId(UUID.randomUUID()),
                        createWalletRequestDto.ownerId(),
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
}
