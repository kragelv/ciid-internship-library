package io.kragelv.library.service;

import java.util.List;
import java.util.UUID;

import io.kragelv.library.model.Reader;

public interface ReaderService {

    Reader createReader(String fullName, String email);

    List<Reader> getAllReaders();

    Reader getReaderById(UUID readerId);

    void updateReader(UUID readerId, String fullName, String email);

    void deleteReader(UUID readerId);
}
