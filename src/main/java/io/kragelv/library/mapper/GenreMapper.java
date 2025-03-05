package io.kragelv.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import io.kragelv.library.dto.genre.GenreDto;
import io.kragelv.library.model.Genre;

@Mapper
public interface GenreMapper {
    
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDto toGenreDto(Genre genre);
}
