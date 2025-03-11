package io.kragelv.library.service;

import java.util.UUID;

import io.kragelv.library.dto.author.AuthorDto;
import io.kragelv.library.dto.author.AuthorRequestDto;
import io.kragelv.library.dto.page.PageResponse;

public interface AuthorService {

    PageResponse<AuthorDto> getList(int page, int limit);

    AuthorDto getById(UUID id);

    UUID create(AuthorRequestDto dto);

    AuthorDto update(UUID id, AuthorRequestDto dto);

    void deleteById(UUID id); 
}
