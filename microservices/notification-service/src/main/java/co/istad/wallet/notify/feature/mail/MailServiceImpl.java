package co.istad.wallet.notify.feature.mail;

import co.istad.wallet.notify.feature.mail.dto.MailRequest;
import co.istad.wallet.notify.feature.mail.dto.MailResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;

    @Async
    @Override
    public CompletableFuture<MailResponse> send(MailRequest mailRequest) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessage.setFrom("noreply@gmail.com");
            mimeMessage.addHeaderLine("This is header line");
            mimeMessageHelper.setTo(mailRequest.to());
            mimeMessageHelper.setSubject(mailRequest.sub());
            mimeMessageHelper.setFrom("noreply@gmail.com");
            mimeMessageHelper.setReplyTo("noreply@gmail.com");
            mimeMessageHelper.setText(mailRequest.text(), mailRequest.isHtml());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
        return CompletableFuture.completedFuture(new MailResponse(
                "Mail is sent successfully"
        ));
    }
}
