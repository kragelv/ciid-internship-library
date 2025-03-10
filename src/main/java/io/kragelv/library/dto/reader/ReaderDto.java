package io.kragelv.library.dto.reader;

import java.time.Instant;

public record ReaderDto(
    String id,
    String fullname,
    String email,
    Instant createdAt
) {
}
