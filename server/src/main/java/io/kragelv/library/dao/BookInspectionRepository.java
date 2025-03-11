package io.kragelv.library.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.kragelv.library.model.BookInspection;

@Repository
public interface BookInspectionRepository extends JpaRepository<BookInspection, UUID> {
    
}
