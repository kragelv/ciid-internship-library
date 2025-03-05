package io.kragelv.library.service.impl;

import io.kragelv.library.service.AuthorService;
import io.kragelv.library.service.BookService;
import io.kragelv.library.service.BorrowingService;
import io.kragelv.library.service.GenreService;
import io.kragelv.library.service.ReaderService;
import io.kragelv.library.service.ServiceFactory;

public class ServiceFactoryImpl implements ServiceFactory {
    private static final GenreService genreService = new GenreServiceImpl();
    private static final AuthorService authorService = new AuthorServiceImpl();
    private static final BookService bookService = new BookServiceImpl();
    private static final ReaderService readerService = new ReaderServiceImpl();
    private static final BorrowingService borrowingService = new BorrowingServiceImpl();

    @Override
    public GenreService getGenreService() {
        return genreService;
    }

    @Override
    public AuthorService getAuthorService() {
        return authorService;
    }

    @Override
    public BookService getBookService() {
        return bookService;
    }

    @Override
    public ReaderService getReaderService() {
        return readerService;
    }

    @Override
    public BorrowingService getBorrowingService() {
        return borrowingService;
    }
}
