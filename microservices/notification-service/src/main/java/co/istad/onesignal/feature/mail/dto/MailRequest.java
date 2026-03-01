package co.istad.onesignal.feature.mail.dto;

public record MailRequest(
        String to,
        String sub,
        String text,
        Boolean isHtml
) {
}
