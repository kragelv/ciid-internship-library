package io.kragelv.library.controller.command.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.kragelv.library.config.Service;
import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.JspPaths;
import io.kragelv.library.dto.author.AuthorDto;
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

public class ViewBookCommand implements Command {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    public ViewBookCommand() {
        ServiceFactory serviceFactory = Service.getFactory();
        bookService = serviceFactory.getBookService();
        genreService = serviceFactory.getGenreService();
        authorService = serviceFactory.getAuthorService();
    }

    public ViewBookCommand(BookService bookService, GenreService genreService, AuthorService authorService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");
        
        UUID id;
        if (idString == null || idString.isBlank()) {
            request.getRequestDispatcher(JspPaths.PAGES_PATH + JspPaths.ERROR_404).forward(request, response);
            return;
        } else {
            try {
                id = UUID.fromString(idString);
            } catch (IllegalArgumentException e) {
                request.getRequestDispatcher(JspPaths.PAGES_PATH + JspPaths.ERROR_404).forward(request, response);
            return;
            }
        }

        Book book = bookService.getBookByIdIcnludeAuthorAndGenres(id);
        
        List<Genre> genres = genreService.getAllGenres();
        List<GenreDto> genreDtos = genres.stream()
                .map(GenreMapper.INSTANCE::toGenreDto)
                .toList();

        List<Author> authors = authorService.getAllAuthors();
        List<AuthorDto> authorDtos = authors.stream()
                .map(AuthorMapper.INSTANCE::toAuthorDto)
                .toList();
        request.setAttribute("genres", genreDtos);
        request.setAttribute("authors", authorDtos);


        String edit = request.getParameter("edit");

        request.setAttribute("book", BookMapper.INSTANCE.toBookDto(book));
        request.setAttribute("edit", edit != null);
        request.getRequestDispatcher(JspPaths.PAGES_PATH + JspPaths.BOOK).forward(request, response);
    }
    
    
}
