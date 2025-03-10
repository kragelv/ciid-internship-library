package io.kragelv.library.service;

import java.util.UUID;

import io.kragelv.library.dto.genre.GenreDto;
import io.kragelv.library.dto.genre.GenreRequestDto;
import io.kragelv.library.dto.page.PageResponse;

public interface GenreService {

    PageResponse<GenreDto> getList(int page, int limit);

    GenreDto getById(UUID id);

    UUID create(GenreRequestDto dto);

    GenreDto update(UUID id, GenreRequestDto dto);

    void deleteById(UUID id); 
}
