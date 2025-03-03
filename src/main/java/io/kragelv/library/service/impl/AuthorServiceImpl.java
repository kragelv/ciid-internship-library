package io.kragelv.library.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.kragelv.library.config.DAO;
import io.kragelv.library.dao.AuthorDAO;
import io.kragelv.library.model.Author;
import io.kragelv.library.service.AuthorService;
import io.kragelv.library.service.exception.AuthorNotFoundException;

public class AuthorServiceImpl implements AuthorService {

    private final AuthorDAO authorDAO;

    public AuthorServiceImpl() {
        this.authorDAO = DAO.getFactory().getAuthorDAO();
    }

    public AuthorServiceImpl(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @Override
    public Author createAuthor(String firstName, String middleName, String lastName, Integer birthYear) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setMiddleName(middleName);
        author.setLastName(lastName);
        author.setBirthYear(birthYear);
        return authorDAO.create(author);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDAO.getAll();
    }

    @Override
    public Author getAuthorById(UUID authorId) {
        return authorDAO.getById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));
    }

    @Override
    public void updateAuthor(UUID authorId, String firstName, String middleName, String lastName, int birthYear) {
        Optional<Author> existingAuthor = authorDAO.getById(authorId);
        if (!existingAuthor.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author authorToUpdate = existingAuthor.get();
        authorToUpdate.setFirstName(firstName);
        authorToUpdate.setMiddleName(middleName);
        authorToUpdate.setLastName(lastName);
        authorToUpdate.setBirthYear(birthYear);
        authorDAO.update(authorToUpdate);
    }

    @Override
    public void deleteAuthor(UUID authorId) {
        Optional<Author> existingAuthor = authorDAO.getById(authorId);
        if (!existingAuthor.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        authorDAO.delete(authorId);
    }
}
