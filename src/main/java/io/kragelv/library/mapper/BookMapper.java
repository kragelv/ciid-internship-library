package io.kragelv.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import io.kragelv.library.dto.book.BookDto;
import io.kragelv.library.model.Book;

@Mapper
public interface BookMapper {
    
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto toBookDto(Book book);
}
