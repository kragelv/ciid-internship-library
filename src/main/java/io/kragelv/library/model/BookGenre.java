package io.kragelv.library.model;

import java.util.UUID;

public class BookGenre {
    private UUID bookId;
    private UUID genreId;

    public BookGenre(UUID bookId, UUID genreId) {
        this.bookId = bookId;
        this.genreId = genreId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public UUID getGenreId() {
        return genreId;
    }

    public void setGenreId(UUID genreId) {
        this.genreId = genreId;
    }
}
