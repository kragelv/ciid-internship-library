package io.kragelv.library.service;

import java.util.List;
import java.util.UUID;

import io.kragelv.library.model.Book;

public interface BookService {
    Book createBook(String title, UUID authorId, int publishedYear, int availableCopies, List<String> genres);

    Book createBookWithGenreIds(String title, UUID authorId, int publishedYear, int availableCopies, List<UUID> genreIds);
    
    List<Book> getAllBooks();

    Book getBookById(UUID bookId);

    void updateBook(UUID bookId, String title, UUID authorId, int publishedYear, int availableCopies, List<UUID> genreIds);

    void deleteBook(UUID bookId);

    List<Book> getBooksByGenre(UUID genreId);

    List<Book> getBooksByAuthor(UUID authorId);
}
