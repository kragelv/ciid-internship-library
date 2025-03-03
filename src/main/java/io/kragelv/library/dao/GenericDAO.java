package io.kragelv.library.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenericDAO<T> {
    Optional<T> getById(UUID id);
    List<T> getAll();
    T create(T entity);
    void update(T entity);
    void delete(UUID id);
}