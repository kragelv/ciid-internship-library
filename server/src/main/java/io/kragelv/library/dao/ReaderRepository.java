package io.kragelv.library.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.kragelv.library.model.Reader;

public interface ReaderRepository extends JpaRepository<Reader, UUID> {

    boolean existsByEmail(String email);
    
}
