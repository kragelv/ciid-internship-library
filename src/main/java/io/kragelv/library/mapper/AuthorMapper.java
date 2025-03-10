package io.kragelv.library.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import io.kragelv.library.dto.author.AuthorDto;
import io.kragelv.library.dto.author.AuthorRequestDto;
import io.kragelv.library.model.Author;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AuthorMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author authorRequestDtoToAuthor(AuthorRequestDto dto);

    AuthorDto authorToAuthorDto(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void updateAuthorFromAuthorRequestDto(AuthorRequestDto dto, @MappingTarget Author author);
}
