package io.kragelv.library.service;

import java.util.UUID;

import io.kragelv.library.dto.borrowing.BorrowingBookRequestDto;
import io.kragelv.library.dto.borrowing.BorrowingDto;
import io.kragelv.library.dto.borrowing.BorrowingReturnBookResponseDto;
import io.kragelv.library.dto.borrowing.BorrowingUpdateDto;
import io.kragelv.library.dto.page.PageResponse;

public interface BorrowingService {

    PageResponse<BorrowingDto> getList(int page, int limit);

    BorrowingDto getById(UUID id);

    UUID borrowBook(BorrowingBookRequestDto dto);

    BorrowingDto update(UUID id, BorrowingUpdateDto dto);

    BorrowingReturnBookResponseDto returnBook(UUID id);

    void deleteById(UUID id); 
}

