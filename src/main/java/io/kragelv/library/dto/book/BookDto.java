package io.kragelv.library.dto.book;

import java.util.List;

import io.kragelv.library.dto.genre.GenreDto;

public record BookDto(
        String id,
        String title,
        AuthorDto author,
        Integer publishedYear,
        Integer availableCopies,
        List<GenreDto> genres) {

    public record AuthorDto(
            String id,
            String firstName,
            String middleName,
            String lastName) {
    }
}
