package co.istad.wallet.command.domain.model;

import co.istad.wallet.command.domain.command.*;
import co.istad.wallet.command.domain.exception.DailyWithdrawalLimitExceededException;
import co.istad.wallet.command.domain.exception.InsufficientBalanceException;
import co.istad.wallet.command.domain.exception.WalletNotActiveException;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import co.istad.wallet.common.vo.*;
import co.istad.wallet.common.event.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Aggregate
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Slf4j
public class Wallet {

    @EqualsAndHashCode.Include
    @AggregateIdentifier
    private WalletId walletId;
    private UserId ownerId;
    private Money balance;
    private WalletStatus status;
    private WalletType type;
    private Money dailyWithdrawalLimit;
    private Money withdrawnToday;
    private LocalDate lastWithdrawalDate;
    private Instant createdAt;

    @CommandHandler
    public Wallet(CreateWalletCommand cmd){
        AggregateLifecycle.apply(new WalletCreatedEvent(
                cmd.walletId(),
                cmd.ownerId(),
                cmd.balance(),
                WalletStatus.ACTIVE,
                cmd.type(),
                new Money(cmd.type().getDailyLimit(), cmd.balance().currency()),
                Instant.now()
        ));
    }

    @EventSourcingHandler
    protected void on(WalletCreatedEvent event){
        log.info("WalletCreatedEvent: {}", event);
        this.walletId = event.walletId();
        this.ownerId = event.ownerId();
        this.balance = event.balance();
        this.status = event.status();
        this.type = event.type();
        this.dailyWithdrawalLimit = event.dailyWithdrawalLimit();
        this.withdrawnToday = new Money(BigDecimal.ZERO, event.balance().currency());
        this.createdAt = event.createdAt();
    }

    @CommandHandler
    public void handle(DepositMoneyCommand cmd){
        assertWalletIsActive();
        assertSameCurrency(cmd.amount());

        Money newBalance = new Money(
                this.balance.balance().add(cmd.amount().balance()),
                cmd.amount().currency()
        );

        AggregateLifecycle.apply(
                new MoneyDepositedEvent(
                        cmd.transactionId(),
                        cmd.walletId(),
                        cmd.amount(),
                        newBalance,
                        LocalDate.now(),
                        Instant.now()
                )
        );
    }

    @EventSourcingHandler
    protected void on(MoneyDepositedEvent moneyDepositedEvent){
        this.balance = moneyDepositedEvent.balance();
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand cmd) {
        assertWalletIsActive();
        assertSufficientBalance(cmd.amount());
        assertSameCurrency(cmd.amount());

        LocalDate today = LocalDate.now();

        BigDecimal newWithdrawnToday;

        if (lastWithdrawalDate == null || today.isAfter(lastWithdrawalDate)) {
            newWithdrawnToday = cmd.amount().balance();
        } else {
            newWithdrawnToday =
                    withdrawnToday.balance().add(cmd.amount().balance());

            if (newWithdrawnToday.compareTo(dailyWithdrawalLimit.balance()) > 0) {
                throw new DailyWithdrawalLimitExceededException("Daily limit exceeded.");
            }
        }

        BigDecimal newBalance =
                this.balance.balance().subtract(cmd.amount().balance());

        AggregateLifecycle.apply(
                new MoneyWithdrawnEvent(
                        new TransactionId(UUID.randomUUID()),
                        cmd.walletId(),
                        cmd.amount(),
                        new Money(newBalance, cmd.amount().currency()),
                        today,
                        new Money(newWithdrawnToday, cmd.amount().currency()),
                        Instant.now()
                )
        );
    }

    @EventSourcingHandler
    protected void on(MoneyWithdrawnEvent event){
        this.balance = event.balance();
        this.lastWithdrawalDate = event.occurredDate();
        this.withdrawnToday = event.withdrawnToday();
    }

    @CommandHandler
    public void handle(TransferMoneyCommand cmd){
        assertWalletIsActive();
        assertSufficientBalance(cmd.amount());
        assertSameCurrency(cmd.amount());

        LocalDate today = LocalDate.now();

        BigDecimal newWithdrawnToday;

        if (lastWithdrawalDate == null || today.isAfter(lastWithdrawalDate)) {
            newWithdrawnToday = cmd.amount().balance();
        } else {
            newWithdrawnToday =
                    withdrawnToday.balance().add(cmd.amount().balance());

            if (newWithdrawnToday.compareTo(dailyWithdrawalLimit.balance()) > 0) {
                throw new DailyWithdrawalLimitExceededException("Daily limit exceeded.");
            }
        }

        BigDecimal newBalance =
                this.balance.balance().subtract(cmd.amount().balance());

        AggregateLifecycle.apply(
                new MoneyDebitedEvent(
                        cmd.transactionId(),
                        cmd.walletId(),
                        new TransferId(UUID.randomUUID()),
                        cmd.toWalletId(),
                        cmd.amount(),
                        new Money(newBalance, cmd.amount().currency()),
                        today,
                        new Money(newWithdrawnToday, cmd.amount().currency()),
                        Instant.now()
                )
        );

    }

    @EventSourcingHandler
    protected void on(MoneyDebitedEvent event){
        this.balance = event.balance();
        this.lastWithdrawalDate = event.occurredDate();
        this.withdrawnToday = event.withdrawnToday();
    }

    @CommandHandler
    public void handle(CreditMoneyCommand cmd){
        BigDecimal newBalance = balance.balance().add(cmd.amount().balance());
        AggregateLifecycle.apply(
                new MoneyCreditedEvent(
                        new TransactionId(UUID.randomUUID()),
                        cmd.walletId(),
                        cmd.fromWalletId(),
                        cmd.transferId(),
                        cmd.amount(),
                        new Money(newBalance, cmd.amount().currency()),
                        LocalDate.now(),
                        Instant.now()
                )
        );
    }

    @EventSourcingHandler
    protected void on(MoneyCreditedEvent event){
        this.balance = event.balance();
    }

    @CommandHandler
    public void handle(FreezeWalletCommand cmd){
        AggregateLifecycle.apply(new WalletFrozenEvent(
                cmd.walletId(),
                LocalDate.now()
        ));
    }

    @EventSourcingHandler
    protected void on(WalletFrozenEvent event){
        this.status = WalletStatus.FROZEN;
    }



}
