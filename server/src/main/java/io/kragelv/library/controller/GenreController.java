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

import io.kragelv.library.dto.genre.GenreDto;
import io.kragelv.library.dto.genre.GenreRequestDto;
import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.service.GenreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GenreDto> getList(@Valid @Positive @RequestParam(name = "page", defaultValue = "1") int page,
            @Valid @Positive @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return genreService.getList(page - 1, limit);
    }

    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody GenreRequestDto dto) {
        final UUID id = genreService.create(dto);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromPath("/api/v1/genres/{id}")
                        .buildAndExpand(id)
                        .encode()
                        .toUri())
                .body(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto update(@PathVariable("id") UUID id, @RequestBody GenreRequestDto dto) {
        return genreService.update(id, dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto getById(@PathVariable("id") UUID id) {
        return genreService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        genreService.deleteById(id);
    }
}
