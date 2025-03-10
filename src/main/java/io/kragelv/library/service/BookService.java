package io.kragelv.library.service;

import java.util.UUID;

import io.kragelv.library.dto.book.BookDto;
import io.kragelv.library.dto.book.BookRequestDto;
import io.kragelv.library.dto.page.PageResponse;

public interface BookService {
    
    PageResponse<BookDto> getList(int page, int limit);

    BookDto getById(UUID id);

    UUID create(BookRequestDto dto);

    BookDto update(UUID id, BookRequestDto dto);

    void deleteById(UUID id); 
}
