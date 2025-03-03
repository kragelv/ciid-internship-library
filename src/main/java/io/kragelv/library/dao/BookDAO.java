package io.kragelv.library.dao;

import java.util.List;
import java.util.UUID;

import io.kragelv.library.model.Book;

public interface BookDAO extends GenericDAO<Book> {

    List<Book> getBooksByAuthor(UUID authorId);
    
}
