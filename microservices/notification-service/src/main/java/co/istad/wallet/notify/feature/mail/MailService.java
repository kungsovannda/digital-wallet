package co.istad.wallet.notify.feature.mail;

import co.istad.wallet.notify.feature.mail.dto.MailRequest;
import co.istad.wallet.notify.feature.mail.dto.MailResponse;

import java.util.concurrent.CompletableFuture;

public interface MailService {

    CompletableFuture<MailResponse> send(MailRequest mailRequest);
}
