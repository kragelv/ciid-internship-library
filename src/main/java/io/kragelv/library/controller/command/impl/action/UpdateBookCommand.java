package io.kragelv.library.controller.command.impl.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.kragelv.library.config.Service;
import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.CommandPaths;
import io.kragelv.library.controller.util.ValidationResult;
import io.kragelv.library.dto.book.UpdateBookDto;
import io.kragelv.library.service.BookService;
import io.kragelv.library.service.ServiceFactory;
import io.kragelv.library.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UpdateBookCommand implements Command {
    private static final Logger logger = LogManager.getLogger(UpdateBookCommand.class);
    private final BookService bookService;

    public UpdateBookCommand() {
        ServiceFactory serviceFactory = Service.getFactory();
        bookService = serviceFactory.getBookService();
    }

    public UpdateBookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    private ValidationResult<UpdateBookDto> extractWithValidate(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        String idString = request.getParameter("id");
        if (idString == null || idString.isBlank()) {
            errors.add("Id is required.");
        }
        UUID id = null;
        try {
            id = UUID.fromString(idString);
        } catch (IllegalArgumentException e) {
            errors.add("Id is invalid.");
        }

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
            publishedYear = (publishedYearString != null && !publishedYearString.isBlank())
                    ? Integer.parseInt(publishedYearString)
                    : null;
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
        List<UUID> genreIds = genreIdsArray != null ? Arrays.stream(genreIdsArray).map(UUID::fromString).toList()
                : List.of();
        return new ValidationResult<>(new UpdateBookDto(id, title, authorId, publishedYear, availableCopies, genreIds),
                errors);
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ValidationResult<UpdateBookDto> validationResult = extractWithValidate(request);
        UpdateBookDto updateBookDto = validationResult.value();
        if (!validationResult.errors().isEmpty()) {
            request.getSession().setAttribute("errors", validationResult.errors());
            response.sendRedirect(String.format("%s?id=%s&edit",
                    request.getContextPath() + CommandPaths.VIEW_BOOK,
                    updateBookDto.id()));
            return;
        }

        List<String> errors = new ArrayList<>();
        try {
            bookService.updateBookWithGenreIds(
                    updateBookDto.id(),
                    updateBookDto.title(),
                    updateBookDto.authorId(),
                    updateBookDto.publishedYear(),
                    updateBookDto.availableCopies(),
                    updateBookDto.genreIds());
        } catch (ServiceException ex) {
            errors.add(ex.getMessage());
        }
        if (!errors.isEmpty()) {
            request.getSession().setAttribute("errors", errors);
            response.sendRedirect(String.format("%s?id=%s&edit",
                    request.getContextPath() + CommandPaths.VIEW_BOOK,
                    updateBookDto.id()));
            return;
        }

        logger.info("Book with id {} was updated", updateBookDto.id());
        response.sendRedirect(String.format("%s?id=%s",
                    request.getContextPath() + CommandPaths.VIEW_BOOK,
                    updateBookDto.id()));
    }
}
