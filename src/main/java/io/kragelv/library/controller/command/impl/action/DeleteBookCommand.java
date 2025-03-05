package io.kragelv.library.controller.command.impl.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.kragelv.library.config.Service;
import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.CommandPaths;
import io.kragelv.library.service.BookService;
import io.kragelv.library.service.ServiceFactory;
import io.kragelv.library.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteBookCommand implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteBookCommand.class);
    private final BookService bookService;

    public DeleteBookCommand() {
        ServiceFactory serviceFactory = Service.getFactory();
        bookService = serviceFactory.getBookService();
    }

    public DeleteBookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

         if (!errors.isEmpty()) {
            request.getSession().setAttribute("errors", errors);
            response.sendRedirect(String.format("%s?id=%s",
                    request.getContextPath() + CommandPaths.VIEW_BOOK,
                    id));
            return;
        }

        try {
            bookService.deleteBook(id);
        } catch (ServiceException ex) {
            errors.add(ex.getMessage());
        }

        if (!errors.isEmpty()) {
            request.getSession().setAttribute("errors", errors);
            response.sendRedirect(String.format("%s?id=%s",
                    request.getContextPath() + CommandPaths.VIEW_BOOK,
                    id));
            return;
        }

        logger.info("Book with id {} has been deleted.", id);
        response.sendRedirect(request.getContextPath() + CommandPaths.LIST_BOOKS);
    }
}
