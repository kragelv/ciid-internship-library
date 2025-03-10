package io.kragelv.library.service.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.kragelv.library.dao.GenreRepository;
import io.kragelv.library.dto.genre.GenreDto;
import io.kragelv.library.dto.genre.GenreRequestDto;
import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.exception.GenreAlreadyExistsException;
import io.kragelv.library.exception.GenreNotFoundException;
import io.kragelv.library.mapper.GenreMapper;
import io.kragelv.library.model.Genre;
import io.kragelv.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<GenreDto> getList(int page, int limit) {
        Page<Genre> genresPage = genreRepository.findAll(PageRequest.of(page, limit));
        log.info("Fetching genres page {} with limit {}", page, limit);
        return PageResponse.create(genresPage, genreMapper::genreToGenreDto);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDto getById(UUID id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));
        log.info("Fetching genre with ID {}", id);
        return genreMapper.genreToGenreDto(genre);
    }

    @Override
    public UUID create(GenreRequestDto dto) {
        if (genreRepository.existsByName(dto.name())) {
            throw new GenreAlreadyExistsException(dto.name(), "name");
        }

        Genre genre = genreMapper.genreRequestDtoToGenre(dto);
        genre = genreRepository.save(genre);
        log.info("Created new genre with ID {}", genre.getId());
        return genre.getId();
    }

    @Override
    public GenreDto update(UUID id, GenreRequestDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));
        if (genreRepository.existsByName(dto.name())) {
            throw new GenreAlreadyExistsException(dto.name(), "name");
        }
        genreMapper.updateGenreFromGenreRequestDto(dto, genre);
        genre = genreRepository.save(genre);
        log.info("Updated genre with ID {}", id);
        return genreMapper.genreToGenreDto(genre);
    }

    @Override
    public void deleteById(UUID id) {
        if (!genreRepository.existsById(id)) {
            throw new GenreNotFoundException(id);
        }
        genreRepository.deleteById(id);
        log.info("Deleted genre with ID {}", id);
    }
}
