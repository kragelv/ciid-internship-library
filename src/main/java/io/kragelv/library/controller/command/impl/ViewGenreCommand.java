package io.kragelv.library.controller.command.impl;

import java.io.IOException;
import java.util.UUID;

import io.kragelv.library.config.Service;
import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.JspPaths;
import io.kragelv.library.mapper.GenreMapper;
import io.kragelv.library.model.Genre;
import io.kragelv.library.service.GenreService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ViewGenreCommand implements Command {

    private final GenreService genreService;

    public ViewGenreCommand() {
        this.genreService = Service.getFactory().getGenreService();
    }

    public ViewGenreCommand(GenreService genreService) {
        this.genreService = genreService;
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

        Genre genre = genreService.getGenreById(id);

        String edit = request.getParameter("edit");

        request.setAttribute("genre", GenreMapper.INSTANCE.toGenreDto(genre));
        request.setAttribute("edit", edit != null);
        request.getRequestDispatcher(JspPaths.PAGES_PATH + JspPaths.GENRE).forward(request, response);
        
    }
}
