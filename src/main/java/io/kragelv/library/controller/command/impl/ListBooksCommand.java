package io.kragelv.library.controller.command.impl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.kragelv.library.config.Service;
import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.JspPaths;
import io.kragelv.library.dto.author.AuthorDto;
import io.kragelv.library.dto.book.BookDto;
import io.kragelv.library.dto.genre.GenreDto;
import io.kragelv.library.mapper.AuthorMapper;
import io.kragelv.library.mapper.BookMapper;
import io.kragelv.library.mapper.GenreMapper;
import io.kragelv.library.model.Author;
import io.kragelv.library.model.Book;
import io.kragelv.library.model.Genre;
import io.kragelv.library.service.AuthorService;
import io.kragelv.library.service.BookService;
import io.kragelv.library.service.GenreService;
import io.kragelv.library.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListBooksCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ListBooksCommand.class);

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    public ListBooksCommand() {
        ServiceFactory serviceFactory = Service.getFactory();
        bookService = serviceFactory.getBookService();
        genreService = serviceFactory.getGenreService();
        authorService = serviceFactory.getAuthorService();
    }

    public ListBooksCommand(BookService bookService, GenreService genreService, AuthorService authorService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Book> books = bookService.getAllBooksIcnludeAuthorAndGenres();
        List<BookDto> bookDtos = books.stream()
                .map(BookMapper.INSTANCE::toBookDto)
                .toList();

        List<Genre> genres = genreService.getAllGenres();
        List<GenreDto> genreDtos = genres.stream()
                .map(GenreMapper.INSTANCE::toGenreDto)
                .toList();

        List<Author> authors = authorService.getAllAuthors();
        List<AuthorDto> authorDtos = authors.stream()
                .map(AuthorMapper.INSTANCE::toAuthorDto)
                .toList();
        request.setAttribute("books", bookDtos);
        request.setAttribute("genres", genreDtos);
        request.setAttribute("authors", authorDtos);
        logger.info("Get list of books. Size: {}", bookDtos.size());
        request.getRequestDispatcher(JspPaths.PAGES_PATH + JspPaths.BOOKS).forward(request, response);
    }
}
