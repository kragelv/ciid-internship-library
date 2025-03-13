package io.kragelv.library.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants.ComponentModel;

import io.kragelv.library.dto.genre.GenreDto;
import io.kragelv.library.dto.genre.GenreRequestDto;
import io.kragelv.library.model.Genre;

@Mapper(componentModel = ComponentModel.SPRING)
public interface GenreMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Genre genreRequestDtoToGenre(GenreRequestDto dto);

    GenreDto genreToGenreDto(Genre genre);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void updateGenreFromGenreRequestDto(GenreRequestDto dto, @MappingTarget Genre genre);
}
