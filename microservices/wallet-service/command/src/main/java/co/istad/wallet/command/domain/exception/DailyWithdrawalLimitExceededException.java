package co.istad.wallet.command.domain.exception;

import org.springframework.http.HttpStatus;

public class DailyWithdrawalLimitExceededException extends WalletException {
    public DailyWithdrawalLimitExceededException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
