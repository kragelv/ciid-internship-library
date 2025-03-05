package io.kragelv.library.dao;

import java.util.Optional;

import io.kragelv.library.model.Reader;

public interface ReaderDAO extends GenericDAO<Reader> {

    Optional<Reader> getByEmail(String email);

}
