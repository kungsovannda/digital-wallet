package co.istad.wallet.command.domain.exception;

import org.springframework.http.HttpStatus;

public class CurrencyMismatchException extends WalletException {
    public CurrencyMismatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
