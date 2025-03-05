package io.kragelv.library.dto.book;

import java.util.List;
import java.util.UUID;

public record UpdateBookDto(
        UUID id,
        String title,
        UUID authorId,
        Integer publishedYear,
        Integer availableCopies,
        List<UUID> genreIds) {
}
