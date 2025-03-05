package io.kragelv.library.controller.command.impl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.kragelv.library.config.Service;
import io.kragelv.library.controller.command.Command;
import io.kragelv.library.controller.command.constant.JspPaths;
import io.kragelv.library.dto.genre.GenreDto;
import io.kragelv.library.mapper.GenreMapper;
import io.kragelv.library.model.Genre;
import io.kragelv.library.service.GenreService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListGenresCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ListGenresCommand.class);
    private final GenreService genreService;

    public ListGenresCommand() {
        this.genreService = Service.getFactory().getGenreService();
    }
    
    public ListGenresCommand(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Genre> genres = genreService.getAllGenres();
        List<GenreDto> genreDtos = genres.stream()
                .map(GenreMapper.INSTANCE::toGenreDto)
                .toList();
        logger.info("Get list of genres. Size: {}", genreDtos.size());
        request.setAttribute("genres", genreDtos);
        request.getRequestDispatcher(JspPaths.PAGES_PATH + JspPaths.GENRES).forward(request, response);

    }
}
