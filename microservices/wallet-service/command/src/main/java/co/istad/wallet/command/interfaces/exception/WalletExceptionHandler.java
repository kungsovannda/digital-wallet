package co.istad.wallet.command.interfaces.exception;

import co.istad.wallet.command.domain.exception.DailyWithdrawalLimitExceededException;
import co.istad.wallet.command.domain.exception.InsufficientBalanceException;
import co.istad.wallet.command.domain.exception.WalletNotActiveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class WalletExceptionHandler {

    @ExceptionHandler(WalletNotActiveException.class)
    public ResponseEntity<?> handle(WalletNotActiveException e){
        return new ResponseEntity<>(
                Map.of(
                        "message", e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handle(InsufficientBalanceException e){
        return new ResponseEntity<>(
                Map.of(
                        "message", e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DailyWithdrawalLimitExceededException.class)
    public ResponseEntity<?> handle(DailyWithdrawalLimitExceededException e){
        return new ResponseEntity<>(
                Map.of(
                        "message", e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
