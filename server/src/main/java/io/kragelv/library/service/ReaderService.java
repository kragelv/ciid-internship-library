package io.kragelv.library.service;

import java.util.UUID;

import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.dto.reader.ReaderDto;
import io.kragelv.library.dto.reader.ReaderRequestDto;

public interface ReaderService {

    PageResponse<ReaderDto> getList(int page, int limit);

    ReaderDto getById(UUID id);

    UUID create(ReaderRequestDto dto);

    ReaderDto update(UUID id, ReaderRequestDto dto);

    void deleteById(UUID id); 
}
