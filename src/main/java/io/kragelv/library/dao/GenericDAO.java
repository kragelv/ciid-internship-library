package io.kragelv.library.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.kragelv.library.dao.exception.DAOException;

public interface GenericDAO<T> {
    Optional<T> getById(UUID id) throws DAOException;
    List<T> getAll() throws DAOException;
    void add(T entity) throws DAOException;
    void update(T entity) throws DAOException;
    void delete(UUID id) throws DAOException;
}