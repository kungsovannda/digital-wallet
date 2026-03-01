package co.istad.wallet.command.domain.exception;

import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends WalletException {
    public InsufficientBalanceException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
