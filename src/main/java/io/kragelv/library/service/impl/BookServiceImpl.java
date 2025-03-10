package io.kragelv.library.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import io.kragelv.library.dao.AuthorRepository;
import io.kragelv.library.dao.BookRepository;
import io.kragelv.library.dao.GenreRepository;
import io.kragelv.library.dto.book.BookDto;
import io.kragelv.library.dto.book.BookRequestDto;
import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.exception.AuthorNotFoundException;
import io.kragelv.library.exception.BookNotFoundException;
import io.kragelv.library.exception.GenreNotFoundException;
import io.kragelv.library.exception.ResourceNotFoundException;
import io.kragelv.library.mapper.BookMapper;
import io.kragelv.library.model.Author;
import io.kragelv.library.model.Book;
import io.kragelv.library.model.Genre;
import io.kragelv.library.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookDto> getList(int page, int limit) {
        Page<Book> booksPage = bookRepository.findAll(PageRequest.of(page, limit));
        log.info("Fetching books page {} with limit {}", page, limit);
        return PageResponse.create(booksPage, bookMapper::bookToBookDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        log.info("Fetching book with ID {}", id);
        return bookMapper.bookToBookDto(book);
    }

    @Override
    public UUID create(BookRequestDto dto) {
        UUID authorId = dto.authorId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        List<Genre> existingGenres = getGenresByIds(dto.genreIds());

        Book book = bookMapper.bookRequestDtoToBook(dto);
        book.setAuthor(author);
        existingGenres.forEach(book::addGenre);
        bookRepository.save(book);

        log.info("Created new book with ID {}", book.getId());
        return book.getId();
    }

    @Override
    public BookDto update(UUID id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        List<Genre> existingGenres = getGenresByIds(dto.genreIds());

        UUID authorId = dto.authorId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        bookMapper.updateBookFromBookRequestDto(dto, book);
        book.setAuthor(author);
        book.getGenres().clear();
        existingGenres.forEach(book::addGenre);

        bookRepository.save(book);

        log.info("Updated book with ID {}", id);
        return bookMapper.bookToBookDto(book);
    }

    private List<Genre> getGenresByIds(List<UUID> genreIds) {
        List<Genre> existingGenres = genreRepository.findByIds(genreIds);
        if (genreIds.size() != existingGenres.size()) {
            List<UUID> existingGenreIds = existingGenres.stream()
                    .map(Genre::getId)
                    .toList();

            Optional<UUID> missingGenreId = genreIds.stream()
                    .filter(genreId -> !existingGenreIds.contains(genreId))
                    .findFirst();

            if (missingGenreId.isPresent()) {
                throw new GenreNotFoundException(missingGenreId.get());
            } else {
                throw new ResourceNotFoundException("Some genres not found");
            }
        }
        return existingGenres;
    }

    @Override
    public void deleteById(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
        log.info("Deleted book with ID {}", id);
    }
}
