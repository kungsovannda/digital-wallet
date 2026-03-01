package co.istad.wallet.notify.handler;

import co.istad.wallet.common.event.*;
import co.istad.wallet.notify.domain.User;
import co.istad.wallet.notify.domain.Wallet;
import co.istad.wallet.notify.feature.mail.MailService;
import co.istad.wallet.notify.feature.mail.dto.MailRequest;
import co.istad.wallet.notify.feature.user.UserRepository;
import co.istad.wallet.notify.feature.wallet.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Year;

@Slf4j
@Component
@ProcessingGroup("notification-processing")
@RequiredArgsConstructor
public class NotificationHandlingComponent {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TemplateEngine templateEngine;
    private final MailService mailService;

    @EventHandler
    public void handle(UserCreatedEvent event) {
        User user = new User(
                event.userId().id().toString(),
                event.username(),
                event.email(),
                event.givenName(),
                event.familyName(),
                event.gender());
        userRepository.save(user);
        sendEmail(user, "Welcome to Digital Wallet", "welcome", null);
    }

    @EventHandler
    public void handle(WalletCreatedEvent event) {
        Wallet wallet = new Wallet(
                event.walletId().id().toString(),
                event.ownerId().id().toString());
        walletRepository.save(wallet);
    }

    @EventHandler
    public void handle(MoneyWithdrawnEvent event) {
        processTransaction(event.walletId().id().toString(), "WITHDRAWAL", event.amount().balance().toString(),
                event.balance().balance().toString(), event.occurredDate().toString());
    }

    @EventHandler
    public void handle(MoneyDepositedEvent event) {
        processTransaction(event.walletId().id().toString(), "DEPOSIT", event.amount().balance().toString(),
                event.balance().balance().toString(), event.occurredDate().toString());
    }

    @EventHandler
    public void handle(MoneyDebitedEvent event) {
        processTransaction(event.walletId().id().toString(), "TRANSFER (DEBIT)", event.amount().balance().toString(),
                event.balance().balance().toString(), event.occurredDate().toString());
    }

    @EventHandler
    public void handle(MoneyCreditedEvent event) {
        processTransaction(event.walletId().id().toString(), "TRANSFER (CREDIT)", event.amount().balance().toString(),
                event.balance().balance().toString(), event.occurredDate().toString());
    }

    @EventHandler
    public void handle(WalletFrozenEvent event) {
        walletRepository.findById(event.walletId().id().toString()).ifPresent(wallet -> {
            userRepository.findById(wallet.getOwnerId()).ifPresent(user -> {
                Context context = new Context();
                context.setVariable("type", "WALLET FROZEN");
                context.setVariable("amount", "N/A");
                context.setVariable("balance", "N/A");
                context.setVariable("date", event.occurredDate().toString());
                sendEmail(user, "Security Alert: Wallet Frozen", "transaction-notice", context);
            });
        });
    }

    private void processTransaction(String walletId, String type, String amount, String balance, String date) {
        walletRepository.findById(walletId).ifPresent(wallet -> {
            userRepository.findById(wallet.getOwnerId()).ifPresent(user -> {
                Context context = new Context();
                context.setVariable("type", type);
                context.setVariable("amount", amount);
                context.setVariable("balance", balance);
                context.setVariable("date", date);
                sendEmail(user, "Transaction Alert: " + type, "transaction-notice", context);
            });
        });
    }

    private void sendEmail(User user, String subject, String template, Context context) {
        if (context == null)
            context = new Context();
        context.setVariable("username", user.getUsername());
        context.setVariable("email", user.getEmail());
        context.setVariable("year", Year.now().getValue());

        String html = templateEngine.process(template, context);
        MailRequest mailRequest = new MailRequest(
                user.getEmail(),
                subject,
                html,
                true);
        mailService.send(mailRequest);
    }
}
