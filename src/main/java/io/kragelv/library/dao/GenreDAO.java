package io.kragelv.library.dao;

import java.util.Optional;

import io.kragelv.library.model.Genre;

public interface GenreDAO extends GenericDAO<Genre> {

    Optional<Genre> getByName(String name);
    
}
