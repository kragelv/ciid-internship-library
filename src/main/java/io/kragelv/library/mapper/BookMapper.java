package io.kragelv.library.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import io.kragelv.library.dto.book.BookDto;
import io.kragelv.library.dto.book.BookRequestDto;
import io.kragelv.library.model.Book;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BookMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "borrowings", ignore = true)
    Book bookRequestDtoToBook(BookRequestDto dto);

    BookDto bookToBookDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "borrowings", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void updateBookFromBookRequestDto(BookRequestDto dto, @MappingTarget Book book);
}
