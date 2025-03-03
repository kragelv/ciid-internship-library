package io.kragelv.library.service;

import java.util.List;
import java.util.UUID;

import io.kragelv.library.model.Author;

public interface AuthorService {
    Author createAuthor(String firstName, String middleName, String lastName, Integer birthYear);

    List<Author> getAllAuthors();

    Author getAuthorById(UUID authorId);

    void updateAuthor(UUID authorId, String firstName, String middleName, String lastName, int birthYear);

    void deleteAuthor(UUID authorId);
}
