package io.kragelv.library.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.kragelv.library.config.DAO;
import io.kragelv.library.dao.GenreDAO;
import io.kragelv.library.model.Genre;
import io.kragelv.library.service.GenreService;
import io.kragelv.library.service.exception.GenreAlreadyExistsException;
import io.kragelv.library.service.exception.GenreNotFoundException;

public class GenreServiceImpl implements GenreService {

    private final GenreDAO genreDAO;

    public GenreServiceImpl() {
        this.genreDAO = DAO.getFactory().getGenreDAO();
    }

    public GenreServiceImpl(GenreDAO genreDAO) {
        this.genreDAO = genreDAO;
    }

    @Override
    public Genre createGenre(String name) {
        Optional<Genre> existingGenre = genreDAO.getByName(name);
        if (existingGenre.isPresent()) {
            throw new GenreAlreadyExistsException(name, "name");
        }

        Genre genre = new Genre();
        genre.setName(name);
        return genreDAO.create(genre);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDAO.getAll();
    }

    @Override
    public Genre getGenreById(UUID genreId) {
        return genreDAO.getById(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));
    }

    @Override
    public void updateGenre(UUID genreId, String newName) {
        Optional<Genre> existingGenre = genreDAO.getById(genreId);
        if (!existingGenre.isPresent()) {
            throw new GenreNotFoundException(genreId);
        }
        Genre genreToUpdate = existingGenre.get();
        genreToUpdate.setName(newName);
        genreDAO.update(genreToUpdate);
    }

    @Override
    public void deleteGenre(UUID genreId) {
        Optional<Genre> existingGenre = genreDAO.getById(genreId);
        if (!existingGenre.isPresent()) {
            throw new GenreNotFoundException(genreId);
        }
        genreDAO.delete(genreId);
    }
}
