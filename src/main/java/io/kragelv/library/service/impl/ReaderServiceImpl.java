package io.kragelv.library.service.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.kragelv.library.dao.ReaderRepository;
import io.kragelv.library.dto.page.PageResponse;
import io.kragelv.library.dto.reader.ReaderDto;
import io.kragelv.library.dto.reader.ReaderRequestDto;
import io.kragelv.library.exception.ReaderAlreadyExistsException;
import io.kragelv.library.exception.ReaderNotFoundException;
import io.kragelv.library.mapper.ReaderMapper;
import io.kragelv.library.model.Reader;
import io.kragelv.library.service.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReaderDto> getList(int page, int limit) {
        Page<Reader> readersPage = readerRepository.findAll(PageRequest.of(page, limit));
        log.info("Fetching readers page {} with limit {}", page, limit);
        return PageResponse.create(readersPage, readerMapper::readerToReaderDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ReaderDto getById(UUID id) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new ReaderNotFoundException(id));
        log.info("Fetching reader with ID {}", id);
        return readerMapper.readerToReaderDto(reader);
    }

    @Override
    public UUID create(ReaderRequestDto dto) {
        if (readerRepository.existsByEmail(dto.email())) {
            throw new ReaderAlreadyExistsException(dto.email(), "email");
        }

        Reader reader = readerMapper.readerRequestDtoToReader(dto);
        reader = readerRepository.save(reader);
        log.info("Created new reader with ID {}", reader.getId());
        return reader.getId();
    }

    @Override
    public ReaderDto update(UUID id, ReaderRequestDto dto) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new ReaderNotFoundException(id));
        if (!Objects.equals(dto.email(), reader.getEmail()) && readerRepository.existsByEmail(dto.email())) {
            throw new ReaderAlreadyExistsException(dto.email(), "email");
        }
        readerMapper.updateReaderFromReaderRequestDto(dto, reader);
        reader = readerRepository.save(reader);
        log.info("Updated reader with ID {}", id);
        return readerMapper.readerToReaderDto(reader);
    }

    @Override
    public void deleteById(UUID id) {
        if (!readerRepository.existsById(id)) {
            throw new ReaderNotFoundException(id);
        }
        readerRepository.deleteById(id);
        log.info("Deleted reader with ID {}", id);
    }
}
