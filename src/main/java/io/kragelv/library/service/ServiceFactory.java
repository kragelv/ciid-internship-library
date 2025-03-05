package io.kragelv.library.service;

public interface ServiceFactory {

    GenreService getGenreService();

    BookService getBookService();

    AuthorService getAuthorService();

    ReaderService getReaderService();

    BorrowingService getBorrowingService();
}
