package io.kragelv.library.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.kragelv.library.dao.BookRepository;
import io.kragelv.library.dao.BorrowingRepository;
import io.kragelv.library.dao.ReaderRepository;
import io.kragelv.library.dto.borrowing.BorrowingBookRequestDto;
import io.kragelv.library.dto.borrowing.BorrowingDto;
import io.kragelv.library.dto.borrowing.BorrowingReturnBookResponseDto;
import io.kragelv.library.dto.borrowing.BorrowingUpdateDto;
import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.exception.BookNotFoundException;
import io.kragelv.library.exception.BorrowingAlreadyReturnedException;
import io.kragelv.library.exception.BorrowingNotFoundException;
import io.kragelv.library.exception.ReaderNotFoundException;
import io.kragelv.library.exception.ReturnedBorrowingModificationException;
import io.kragelv.library.mapper.BorrowingMapper;
import io.kragelv.library.model.Book;
import io.kragelv.library.model.Borrowing;
import io.kragelv.library.model.Reader;
import io.kragelv.library.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final BorrowingMapper borrowingMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BorrowingDto> getList(int page, int limit) {
        Page<Borrowing> borrowingsPage = borrowingRepository.findAll(PageRequest.of(page, limit));
        log.info("Fetching borrowings page {} with limit {}", page, limit);
        return PageResponse.create(borrowingsPage, borrowingMapper::borrowingToBorrowingDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowingDto getById(UUID id) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new BorrowingNotFoundException(id));
        log.info("Fetching borrowing with ID {}", id);
        return borrowingMapper.borrowingToBorrowingDto(borrowing);
    }

    @Override
    public UUID borrowBook(BorrowingBookRequestDto dto) {
        UUID bookId = dto.bookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        UUID readerId = dto.readerId();
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ReaderNotFoundException(readerId));

        Borrowing borrowing = borrowingMapper.borrowingBookDtoToBorrowing(dto);
        borrowing.setBook(book);
        borrowing.setReader(reader);
        borrowing.setBorrowedAt(Instant.now());
        borrowing.setReturnedAt(null);
        borrowing = borrowingRepository.save(borrowing);
        log.info("Created new borrowing with ID {}", borrowing.getId());
        return borrowing.getId();
    }

    @Override
    public BorrowingDto update(UUID id, BorrowingUpdateDto dto) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new BorrowingNotFoundException(id));

        Instant returnedAt = borrowing.getReturnedAt();
        if (returnedAt != null) {
            throw new ReturnedBorrowingModificationException(id, returnedAt);
        }
        
        borrowingMapper.updateBorrowingFromBorrowingRequestDto(dto, borrowing);
        borrowing = borrowingRepository.save(borrowing);
        log.info("Updated borrowing with ID {}", id);
        return borrowingMapper.borrowingToBorrowingDto(borrowing);
    }

    @Override
    public BorrowingReturnBookResponseDto returnBook(UUID id) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new BorrowingNotFoundException(id));

        Instant returnedAt = borrowing.getReturnedAt();
        if (returnedAt != null) {
            throw new BorrowingAlreadyReturnedException(id, returnedAt);
        }

        borrowing.setReturnedAt(Instant.now());
        borrowingRepository.save(borrowing);
        log.info("Returned book with ID {}", id);
        return borrowingMapper.borrowingToBorrowingReturnBookResponseDto(borrowing);
    }

    @Override
    public void deleteById(UUID id) {
        if (!borrowingRepository.existsById(id)) {
            throw new BorrowingNotFoundException(id);
        }
        borrowingRepository.deleteById(id);
        log.info("Deleted borrowing with ID {}", id);
    }
}
