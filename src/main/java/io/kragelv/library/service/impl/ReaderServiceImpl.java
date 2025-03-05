package io.kragelv.library.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.kragelv.library.config.DAO;
import io.kragelv.library.dao.ReaderDAO;
import io.kragelv.library.model.Reader;
import io.kragelv.library.service.ReaderService;
import io.kragelv.library.service.exception.ReaderAlreadyExistsException;
import io.kragelv.library.service.exception.ReaderNotFoundException;

public class ReaderServiceImpl implements ReaderService {
    private final ReaderDAO readerDAO;

    public ReaderServiceImpl() {
        this.readerDAO = DAO.getFactory().getReaderDAO();
    }

    public ReaderServiceImpl(ReaderDAO readerDAO) {
        this.readerDAO = readerDAO;
    }

    @Override
    public Reader createReader(String fullName, String email) {
        Optional<Reader> maybeReader = readerDAO.getByEmail(email);
        if (maybeReader.isPresent()) {
            throw new ReaderAlreadyExistsException(email, "email");
        }
        Reader reader = new Reader();
        reader.setFullname(fullName);
        reader.setEmail(email);
        return readerDAO.create(reader);
    }

    @Override
    public List<Reader> getAllReaders() {
        return readerDAO.getAll();
    }

    @Override
    public Reader getReaderById(UUID readerId) {
        return readerDAO.getById(readerId)
                .orElseThrow(() -> new ReaderNotFoundException(readerId));
    }

    @Override
    public void updateReader(UUID readerId, String fullName, String email) {
        Optional<Reader> existingReader = readerDAO.getById(readerId);
        if (existingReader.isEmpty()) {
            throw new ReaderNotFoundException(readerId);
        }

        Reader readerToUpdate = new Reader();
        readerToUpdate.setFullname(fullName);
        readerToUpdate.setEmail(email);
        readerDAO.update(readerToUpdate);
    }

    @Override
    public void deleteReader(UUID readerId) {
        Optional<Reader> existingReader = readerDAO.getById(readerId);
        if (existingReader.isEmpty()) {
            throw new ReaderNotFoundException(readerId);
        }

        readerDAO.delete(readerId);
    }
}
