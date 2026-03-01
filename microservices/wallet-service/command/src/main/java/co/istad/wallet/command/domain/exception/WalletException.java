package co.istad.wallet.command.domain.exception;

import org.springframework.http.HttpStatus;

public abstract class WalletException extends RuntimeException {
    private final HttpStatus status;

    protected WalletException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }
}
