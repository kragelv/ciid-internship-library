package io.kragelv.library.dto.book;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequestDto(

        @NotBlank
        @Size(max = 255)
        String title,

        UUID authorId,

        @Min(0)
        Integer publishedYear,

        @Min(0)
        @NotNull
        Integer availableCopies,

        @Size(max = 10)
        @NotNull
        List<UUID> genreIds) {
}
