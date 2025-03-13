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

import io.kragelv.library.dto.book.BookDto;
import io.kragelv.library.dto.book.BookRequestDto;
import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<BookDto> getList(@Valid @Positive @RequestParam(name = "page", defaultValue = "1") int page,
            @Valid @Positive @RequestParam(name = "limit", defaultValue = "15") int limit) {
        return bookService.getList(page - 1, limit);
    }

    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody BookRequestDto dto) {
        final UUID id = bookService.create(dto);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromPath("/api/v1/books/{id}")
                        .buildAndExpand(id)
                        .encode()
                        .toUri())
                .body(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto update(@PathVariable("id") UUID id, @RequestBody BookRequestDto dto) {
        return bookService.update(id, dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getById(@PathVariable("id") UUID id) {
        return bookService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        bookService.deleteById(id);
    }
}
