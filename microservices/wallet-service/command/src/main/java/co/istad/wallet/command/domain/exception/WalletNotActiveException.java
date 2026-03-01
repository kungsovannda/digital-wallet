package co.istad.wallet.command.domain.exception;

import org.springframework.http.HttpStatus;

public class WalletNotActiveException extends WalletException {
    public WalletNotActiveException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
