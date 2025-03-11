package io.kragelv.library.service.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.kragelv.library.dao.AuthorRepository;
import io.kragelv.library.dto.author.AuthorDto;
import io.kragelv.library.dto.author.AuthorRequestDto;
import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.exception.AuthorNotFoundException;
import io.kragelv.library.mapper.AuthorMapper;
import io.kragelv.library.model.Author;
import io.kragelv.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuthorDto> getList(int page, int limit) {
        Page<Author> authorsPage = authorRepository.findAll(PageRequest.of(page, limit));
        log.info("Fetching authors page {} with limit {}", page, limit);
        return PageResponse.create(authorsPage, authorMapper::authorToAuthorDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        log.info("Fetching author with ID {}", id);
        return authorMapper.authorToAuthorDto(author);
    }

    @Override
    public UUID create(AuthorRequestDto dto) {
        Author author = authorMapper.authorRequestDtoToAuthor(dto);
        author = authorRepository.save(author);
        log.info("Created new author with ID {}", author.getId());
        return author.getId();
    }

    @Override
    public AuthorDto update(UUID id, AuthorRequestDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        authorMapper.updateAuthorFromAuthorRequestDto(dto, author);
        author = authorRepository.save(author);
        log.info("Updated author with ID {}", id);
        return authorMapper.authorToAuthorDto(author);
    }

    @Override
    public void deleteById(UUID id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
        authorRepository.deleteById(id);
        log.info("Deleted author with ID {}", id);
    }
}

