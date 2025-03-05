package io.kragelv.library.controller.command.impl.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.kragelv.library.config.Service;
import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.CommandPaths;
import io.kragelv.library.controller.util.ValidationResult;
import io.kragelv.library.dto.genre.CreateGenreDto;
import io.kragelv.library.service.GenreService;
import io.kragelv.library.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreateGenreCommand implements Command {

    private final GenreService genreService;

    public CreateGenreCommand() {
        this.genreService = Service.getFactory().getGenreService();
    }

    public CreateGenreCommand(GenreService genreService) {
        this.genreService = genreService;
    }

    private ValidationResult<CreateGenreDto> extractWithValidate(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        
        String name = request.getParameter("name");
        if (name == null || name.isBlank()) {
            errors.add("Name is required.");
        }
        
        return new ValidationResult<>(new CreateGenreDto(name), errors);
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        ValidationResult<CreateGenreDto> validationResult = extractWithValidate(request);
        if (!validationResult.errors().isEmpty()) {
            request.getSession().setAttribute("errors", validationResult.errors());
            response.sendRedirect(request.getContextPath() + CommandPaths.LIST_GENRES);
            return;
        }

        List<String> errors = new ArrayList<>();
        CreateGenreDto createGenreDto = validationResult.value();
      
        try {
            genreService.createGenre(createGenreDto.name());
        } catch (ServiceException ex) {
            errors.add(ex.getMessage());
        }

        if (!errors.isEmpty()) {
            request.getSession().setAttribute("errors", errors);
            response.sendRedirect(request.getContextPath() + CommandPaths.LIST_GENRES);
            return;
        }
        
        response.sendRedirect(request.getContextPath() + CommandPaths.LIST_GENRES);
    }
}
