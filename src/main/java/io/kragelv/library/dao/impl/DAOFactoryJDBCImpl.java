package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.AuthorDAO;
import io.kragelv.library.dao.BookDAO;
import io.kragelv.library.dao.BookGenreDAO;
import io.kragelv.library.dao.BorrowingDAO;
import io.kragelv.library.dao.DAOFactory;
import io.kragelv.library.dao.GenreDAO;
import io.kragelv.library.dao.ReaderDAO;

public class DAOFactoryJDBCImpl implements DAOFactory {
    private static final AuthorDAO authorDAO = new AuthorDAOImpl();
    private static final BorrowingDAO borrowingDAO = new BorrowingDAOImpl();
    private static final BookDAO bookDAO = new BookDAOImpl();
    private static final GenreDAO genreDAO = new GenreDAOImpl();
    private static final ReaderDAO readerDAO = new ReaderDAOImpl();
    private static final BookGenreDAO bookGenreDAO = new BookGenreDAOImpl();

    @Override
    public AuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    @Override
    public BorrowingDAO getBorrowingDAO() {
        return borrowingDAO;
    }

    @Override
    public BookDAO getBookDAO() {
        return bookDAO;
    }

    @Override
    public GenreDAO getGenreDAO() {
        return genreDAO;
    }

    @Override
    public ReaderDAO getReaderDAO() {
        return readerDAO;
    }

    @Override
    public BookGenreDAO getBookGenreDAO() {
        return bookGenreDAO;
    }
    
}
