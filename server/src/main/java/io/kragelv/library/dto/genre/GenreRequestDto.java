package io.kragelv.library.dto.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreRequestDto(

    @NotBlank
    @Size(min = 1, max = 255)
    String name
) {
}
