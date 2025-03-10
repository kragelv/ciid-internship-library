package io.kragelv.library.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.kragelv.library.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {

    boolean existsByName(String name);

    @Query("SELECT g FROM Genre g WHERE g.id IN :ids")
    List<Genre> findByIds(@Param("ids") List<UUID> ids);

}
