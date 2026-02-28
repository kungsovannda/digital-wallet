package co.istad.wallet.command.interfaces.exception;

import org.axonframework.modelling.command.AggregateNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AxonExceptionHandler {

    @ExceptionHandler(AggregateNotFoundException.class)
    public ResponseEntity<?> handle(AggregateNotFoundException e){
        return new ResponseEntity<>(
                Map.of(
                        "message", e.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
