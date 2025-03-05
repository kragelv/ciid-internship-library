package io.kragelv.library.controller.command.impl.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.kragelv.library.config.Service;
import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.CommandPaths;
import io.kragelv.library.controller.util.ValidationResult;
import io.kragelv.library.dto.book.CreateBookDto;
import io.kragelv.library.service.BookService;
import io.kragelv.library.service.ServiceFactory;
import io.kragelv.library.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreateBookCommand implements Command {

    private final BookService bookService;

    public CreateBookCommand() {
        ServiceFactory serviceFactory = Service.getFactory();
        bookService = serviceFactory.getBookService();
    }

    public CreateBookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    private ValidationResult<CreateBookDto> extractWithValidate(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        
        String title = request.getParameter("title");
        if (title == null || title.isBlank()) {
            errors.add("Title is required.");
        }
        String authorIdString = request.getParameter("authorId");
        if (authorIdString == null || authorIdString.isBlank()) {
            errors.add("Author is required.");
        }
        UUID authorId = null;
        try {
            authorId = UUID.fromString(authorIdString);
        } catch (IllegalArgumentException e) {
            errors.add("Author is invalid.");
        }
        String publishedYearString = request.getParameter("publishedYear");
        Integer publishedYear = null;
        try {
            publishedYear = (publishedYearString != null && !publishedYearString.isBlank()) ? Integer.parseInt(publishedYearString) : null;
        } catch (NumberFormatException e) {
            errors.add("Published year is invalid");
        }
        
        String availableCopiesString = request.getParameter("availableCopies");
        Integer availableCopies = null;
        if (availableCopiesString == null || availableCopiesString.isBlank()) {
            errors.add("Available copies is invalid");
        } else {
            try {
                availableCopies = Integer.parseInt(availableCopiesString);
            } catch (NumberFormatException e) {
                errors.add("Available copies is invalid");
            }
        }
       
        String[] genreIdsArray = request.getParameterValues("genres");
        List<UUID> genreIds = genreIdsArray != null ? 
            Arrays.stream(genreIdsArray).map(UUID::fromString).toList() :
            List.of();
        return new ValidationResult<>(new CreateBookDto(title, authorId, publishedYear, availableCopies, genreIds), errors);
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        ValidationResult<CreateBookDto> validationResult = extractWithValidate(request);
        if (!validationResult.errors().isEmpty()) {
            request.getSession().setAttribute("errors", validationResult.errors());
            response.sendRedirect(request.getContextPath() + CommandPaths.LIST_BOOKS);
            return;
        }
        
        List<String> errors = new ArrayList<>();
        CreateBookDto createBookDto = validationResult.value();
        
        try {
            bookService.createBookWithGenreIds(createBookDto.title(),
            createBookDto.authorId(),
            createBookDto.publishedYear(),
            createBookDto.availableCopies(),
            createBookDto.genreIds());
        } catch (ServiceException ex) {
            errors.add(ex.getMessage());
        }
        
        if (!errors.isEmpty()) {
            request.getSession().setAttribute("errors", errors);
            response.sendRedirect(request.getContextPath() + CommandPaths.LIST_BOOKS);
            return;
        }

        response.sendRedirect(request.getContextPath() + CommandPaths.LIST_BOOKS);  
    }
}
