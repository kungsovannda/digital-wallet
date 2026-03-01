package co.istad.onesignal.feature.mail;

import co.istad.onesignal.feature.mail.dto.*;

public interface MailService {

    MailResponse send(MailRequest mailRequest);
}
