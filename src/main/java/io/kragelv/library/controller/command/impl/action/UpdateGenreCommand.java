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
import io.kragelv.library.controller.util.ValidationResult;
import io.kragelv.library.dto.genre.UpdateGenreDto;
import io.kragelv.library.service.GenreService;
import io.kragelv.library.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UpdateGenreCommand implements Command {
    private static final Logger logger = LogManager.getLogger(UpdateGenreCommand.class);
    private final GenreService genreService;

    public UpdateGenreCommand() {
        this.genreService = Service.getFactory().getGenreService();
    } 

    public UpdateGenreCommand(GenreService genreService) {
        this.genreService = genreService;
    }

    private ValidationResult<UpdateGenreDto> extractWithValidate(HttpServletRequest request) {
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

        String name = request.getParameter("name");
        if (name == null || name.isBlank()) {
            errors.add("Name is required.");
        }
      
        return new ValidationResult<>(new UpdateGenreDto(id, name), errors);
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ValidationResult<UpdateGenreDto> validationResult = extractWithValidate(request);
        UpdateGenreDto updateGenreDto = validationResult.value();
        if (!validationResult.errors().isEmpty()) {
            request.getSession().setAttribute("errors", validationResult.errors());
            response.sendRedirect(String.format("%s?id=%s&edit",
                    request.getContextPath() + CommandPaths.VIEW_GENRE,
                    updateGenreDto.id()));
            return;
        }

        List<String> errors = new ArrayList<>();
        try {
            genreService.updateGenre(
                    updateGenreDto.id(),
                    updateGenreDto.name());
        } catch (ServiceException ex) {
            errors.add(ex.getMessage());
        }
        if (!errors.isEmpty()) {
            request.getSession().setAttribute("errors", errors);
            response.sendRedirect(String.format("%s?id=%s&edit",
                    request.getContextPath() + CommandPaths.VIEW_GENRE,
                    updateGenreDto.id()));
            return;
        }

        logger.info("Genre with id {} was updated.", updateGenreDto.id());
        response.sendRedirect(String.format("%s?id=%s",
                    request.getContextPath() + CommandPaths.VIEW_GENRE,
                    updateGenreDto.id()));
        
    }
}
