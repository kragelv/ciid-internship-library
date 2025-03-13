package io.kragelv.library.dto.error;

import java.time.Instant;

public record ErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        String message
) {
}