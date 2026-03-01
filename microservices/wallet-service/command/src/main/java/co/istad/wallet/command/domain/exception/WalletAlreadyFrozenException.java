package co.istad.wallet.command.domain.exception;

import org.springframework.http.HttpStatus;

public class WalletAlreadyFrozenException extends WalletException {
    public WalletAlreadyFrozenException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
