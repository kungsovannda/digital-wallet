package co.istad.wallet.command.interfaces.rest;

import co.istad.wallet.command.domain.command.*;
import co.istad.wallet.command.interfaces.dto.*;
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
        public ResponseEntity<WalletId> createWallet(@AuthenticationPrincipal Jwt jwt,
                        @Valid @RequestBody CreateWalletRequestDto createWalletRequestDto) {
                log.info("Create wallet user: {}", jwt.getClaims());
                WalletId result = commandGateway.sendAndWait(
                                new CreateWalletCommand(
                                                new WalletId(UUID.randomUUID()),
                                                extractUserId(jwt),
                                                createWalletRequestDto.balance(),
                                                createWalletRequestDto.type()));
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(result);
        }

        @PutMapping("/{walletId}/deposit")
        public ResponseEntity<?> depositMoney(@PathVariable UUID walletId,
                        @Valid @RequestBody DepositMoneyRequestDto depositMoneyRequestDto) {
                WalletId result = commandGateway.sendAndWait(
                                new DepositMoneyCommand(
                                                new WalletId(walletId),
                                                new TransactionId(UUID.randomUUID()),
                                                depositMoneyRequestDto.amount()));
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(result);
        }

        @PutMapping("/{walletId}/withdraw")
        public ResponseEntity<WalletId> withdrawMoney(@PathVariable UUID walletId,
                        @Valid @RequestBody WithdrawMoneyRequestDto withdrawMoneyRequestDto) {
                WalletId result = commandGateway.sendAndWait(
                                new WithdrawMoneyCommand(
                                                new WalletId(walletId),
                                                new TransactionId(UUID.randomUUID()),
                                                withdrawMoneyRequestDto.amount()));
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(result);
        }

        @PutMapping("/{walletId}/transfer")
        public ResponseEntity<WalletId> transferMoney(@PathVariable UUID walletId,
                        @Valid @RequestBody TransferMoneyRequestDto transferMoneyRequestDto) {
                WalletId result = commandGateway.sendAndWait(
                                new TransferMoneyCommand(
                                                new WalletId(walletId),
                                                new TransactionId(UUID.randomUUID()),
                                                new TransferId(UUID.randomUUID()),
                                                transferMoneyRequestDto.amount(),
                                                transferMoneyRequestDto.toWalletId()));
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(result);
        }

        @PutMapping("/{walletId}/freeze")
        public ResponseEntity<WalletId> freezeWallet(@PathVariable UUID walletId) {
                WalletId result = commandGateway.sendAndWait(
                                new FreezeWalletCommand(
                                                new WalletId(walletId)));
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(result);
        }

        private UserId extractUserId(Jwt jwt) {
                return new UserId(
                                UUID.fromString(jwt.getClaims().get("uuid").toString()));
        }
}
