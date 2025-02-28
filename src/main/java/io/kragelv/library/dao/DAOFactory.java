package io.kragelv.library.dao;

public interface DAOFactory {
    AuthorDAO getAuthorDAO();

    BorrowingDAO getBorrowingDAO();

    BookDAO getBookDAO();

    GenreDAO getGenreDAO();

    ReaderDAO getReaderDAO();
}
