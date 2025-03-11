package io.kragelv.library.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.kragelv.library.dto.borrowing.BorrowingBookRequestDto;
import io.kragelv.library.dto.borrowing.BorrowingDto;
import io.kragelv.library.dto.borrowing.BorrowingReturnBookResponseDto;
import io.kragelv.library.dto.borrowing.BorrowingUpdateDto;
import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.service.BorrowingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<BorrowingDto> getList(@Valid @Positive @RequestParam(name = "page", defaultValue = "1") int page,
            @Valid @Positive @RequestParam(name = "limit", defaultValue = "15") int limit) {
        return borrowingService.getList(page - 1, limit);
    }

    @PostMapping
    public ResponseEntity<UUID> borrowBook(@Valid @RequestBody BorrowingBookRequestDto dto) {
        final UUID id = borrowingService.borrowBook(dto);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromPath("/api/v1/borrowings/{id}")
                        .buildAndExpand(id)
                        .encode()
                        .toUri())
                .body(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BorrowingDto update(@PathVariable("id") UUID id, @RequestBody BorrowingUpdateDto dto) {
        return borrowingService.update(id, dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BorrowingDto getById(@PathVariable("id") UUID id) {
        return borrowingService.getById(id);
    }

    @PostMapping("/{id}/return")
    @ResponseStatus(HttpStatus.OK)
    public BorrowingReturnBookResponseDto returnBook(@PathVariable("id") UUID id) {
        return borrowingService.returnBook(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        borrowingService.deleteById(id);
    }
}
