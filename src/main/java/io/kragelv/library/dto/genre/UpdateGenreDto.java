package io.kragelv.library.dto.genre;

import java.util.UUID;

public record UpdateGenreDto(
        UUID id,
        String name) {
}
