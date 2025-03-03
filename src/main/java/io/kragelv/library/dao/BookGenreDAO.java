package io.kragelv.library.dao;

import java.util.List;
import java.util.UUID;

public interface BookGenreDAO {
    void create(UUID bookId, UUID genreId);

    void deleteByBookId(UUID bookId);

    void deleteByGenreId(UUID genreId);

    List<UUID> getGenresByBook(UUID bookId);

    List<UUID> getBooksByGenre(UUID genreId);
}

