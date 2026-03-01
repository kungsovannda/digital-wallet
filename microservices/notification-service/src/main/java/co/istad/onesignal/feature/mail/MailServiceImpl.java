package co.istad.onesignal.feature.mail;

import co.istad.onesignal.feature.mail.dto.MailRequest;
import co.istad.onesignal.feature.mail.dto.MailResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;

    @Override
    public MailResponse send(MailRequest mailRequest) {
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
        return null;
    }
}
