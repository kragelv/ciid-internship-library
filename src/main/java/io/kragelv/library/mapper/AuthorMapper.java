package io.kragelv.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import io.kragelv.library.dto.author.AuthorDto;
import io.kragelv.library.model.Author;

@Mapper
public interface AuthorMapper {
    
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDto toAuthorDto(Author author);
}
