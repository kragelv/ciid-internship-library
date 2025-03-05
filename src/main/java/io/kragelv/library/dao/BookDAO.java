package io.kragelv.library.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.kragelv.library.model.Book;

public interface BookDAO extends GenericDAO<Book> {

    Optional<Book> getByIdIncludeAuthorAndGenres(UUID id);

    List<Book> getBooksByAuthor(UUID authorId);

    List<Book> getAllBooksIncludeAuthorAndGenres();
    
}
