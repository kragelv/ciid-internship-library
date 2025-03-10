package io.kragelv.library.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.kragelv.library.model.Book;

public interface PositionRepository extends JpaRepository<Book, UUID> {
    
}
