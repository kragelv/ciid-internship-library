package io.kragelv.library.service;

import java.util.List;
import java.util.UUID;

import io.kragelv.library.model.Genre;

public interface GenreService {
    Genre createGenre(String name);

    List<Genre> getAllGenres();

    Genre getGenreById(UUID genreId);

    void updateGenre(UUID genreId, String newName);

    void deleteGenre(UUID genreId);
}
