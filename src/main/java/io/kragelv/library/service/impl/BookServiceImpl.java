package io.kragelv.library.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.kragelv.library.config.DAO;
import io.kragelv.library.dao.AuthorDAO;
import io.kragelv.library.dao.BookDAO;
import io.kragelv.library.dao.BookGenreDAO;
import io.kragelv.library.dao.DAOFactory;
import io.kragelv.library.dao.GenreDAO;
import io.kragelv.library.model.Author;
import io.kragelv.library.model.Book;
import io.kragelv.library.model.Genre;
import io.kragelv.library.service.BookService;
import io.kragelv.library.service.exception.AuthorNotFoundException;
import io.kragelv.library.service.exception.BookNotFoundException;
import io.kragelv.library.service.exception.GenreNotFoundException;

public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final GenreDAO genreDAO;
    private final AuthorDAO authorDAO;
    private final BookGenreDAO bookGenreDAO;

    public BookServiceImpl() {
        DAOFactory daoFactory = DAO.getFactory();
        this.bookDAO = daoFactory.getBookDAO();
        this.genreDAO = daoFactory.getGenreDAO();
        this.authorDAO = daoFactory.getAuthorDAO();
        this.bookGenreDAO = daoFactory.getBookGenreDAO();
    }

    public BookServiceImpl(BookDAO bookDAO, GenreDAO genreDAO, AuthorDAO authorDAO, BookGenreDAO bookGenreDAO) {
        this.bookDAO = bookDAO;
        this.genreDAO = genreDAO;
        this.authorDAO = authorDAO;
        this.bookGenreDAO = bookGenreDAO;
    }

    @Override
    public Book createBook(String title, UUID authorId, Integer publishedYear, Integer availableCopies, List<String> genres) {
        Optional<Author> author = authorDAO.getById(authorId);
        if (author.isEmpty()) {
            throw new AuthorNotFoundException(authorId);
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthorId(authorId);
        book.setAvailableCopies(availableCopies);
        book = bookDAO.create(book);

        for (String genreName : genres) {
            Optional<Genre> genre = genreDAO.getByName(genreName);
            UUID genreId;
            if (genre.isEmpty()) {
                Genre newGenre = new Genre();
                newGenre.setName(genreName);
                Genre createdGenre = genreDAO.create(newGenre);
                genreId = createdGenre.getId();
            } else {
                genreId = genre.get().getId();
            }

            bookGenreDAO.create(book.getId(), genreId);
        }

        return book;
    }

    @Override
    public Book createBookWithGenreIds(String title, UUID authorId, Integer publishedYear, Integer availableCopies,
            List<UUID> genreIds) {
        Optional<Author> author = authorDAO.getById(authorId);
        if (author.isEmpty()) {
            throw new AuthorNotFoundException(authorId);
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthorId(authorId);
        book.setAvailableCopies(availableCopies);
        book = bookDAO.create(book);

        for (UUID genreId : genreIds) {
            Optional<Genre> genre = genreDAO.getById(genreId);
            if (genre.isEmpty()) {
                throw new GenreNotFoundException(genreId);
            }

            bookGenreDAO.create(book.getId(), genre.get().getId());
        }

        return book;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDAO.getAll();
    }

    @Override
    public Book getBookById(UUID bookId) {
        return bookDAO.getById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @Override
    public void updateBook(UUID bookId, String title, UUID authorId, Integer publishedYear, Integer availableCopies,
            List<String> genres) {
        Optional<Book> existingBook = bookDAO.getById(bookId);
        if (existingBook.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }

        Optional<Author> author = authorDAO.getById(authorId);
        if (author.isEmpty()) {
            throw new AuthorNotFoundException(authorId);
        }

        Book bookToUpdate = existingBook.get();
        bookToUpdate.setTitle(title);
        bookToUpdate.setAuthorId(authorId);
        bookToUpdate.setPublishedYear(publishedYear);
        bookToUpdate.setAvailableCopies(availableCopies);
        bookDAO.update(bookToUpdate);

        bookGenreDAO.deleteByBookId(bookId);
        for (String genreName : genres) {
            Optional<Genre> genre = genreDAO.getByName(genreName);
            if (genre.isEmpty()) {
                throw new GenreNotFoundException(genreName, "name");
            }

            bookGenreDAO.create(bookId, genre.get().getId());
        }
    }

    @Override
    public void deleteBook(UUID bookId) {
        Optional<Book> book = bookDAO.getById(bookId);
        if (book.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }

        bookGenreDAO.deleteByBookId(bookId);
        bookDAO.delete(bookId);
    }

    @Override
    public List<Book> getBooksByGenre(UUID genreId) {
        Optional<Genre> genre = genreDAO.getById(genreId);
        if (genre.isEmpty()) {
            throw new GenreNotFoundException(genreId);
        }

        return bookGenreDAO.getBooksByGenre(genreId).stream()
                .map(bookId -> bookDAO.getById(bookId)
                        .orElseThrow(() -> new BookNotFoundException(bookId)))
                .toList();
    }

    @Override
    public List<Book> getBooksByAuthor(UUID authorId) {
        Optional<Author> author = authorDAO.getById(authorId);
        if (author.isEmpty()) {
            throw new AuthorNotFoundException(authorId);
        }

        return bookDAO.getBooksByAuthor(authorId);
    }
}
