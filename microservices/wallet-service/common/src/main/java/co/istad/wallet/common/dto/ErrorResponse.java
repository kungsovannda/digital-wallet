package co.istad.wallet.common.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(Object message, String error, Instant timestamp) {
    public static ErrorResponse of(Object message, String error) {
        return new ErrorResponse(message, error, Instant.now());
    }
}