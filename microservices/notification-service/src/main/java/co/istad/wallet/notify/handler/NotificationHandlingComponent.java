package co.istad.wallet.notify.handler;

import co.istad.wallet.common.event.UserCreatedEvent;
import co.istad.wallet.notify.domain.User;
import co.istad.wallet.notify.feature.mail.MailService;
import co.istad.wallet.notify.feature.mail.dto.MailRequest;
import co.istad.wallet.notify.feature.user.UserRepository;
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
    private final TemplateEngine templateEngine;
    private final MailService mailService;

    @EventHandler
    public void handle(UserCreatedEvent event){
        User user = new User(
                event.userId().id().toString(),
                event.username(),
                event.email(),
                event.givenName(),
                event.familyName(),
                event.gender()
        );
        userRepository.save(user);
        Context context = new Context();
        context.setVariable("username", user.getUsername());
        context.setVariable("email", user.getEmail());
        context.setVariable("year", Year.now().getValue());

        String html = templateEngine.process("welcome", context);
        MailRequest mailRequest = new MailRequest(
                user.getEmail(),
                "Welcome to Digital Wallet",
                html,
                true
        );
        mailService.send(mailRequest);
    }
}
