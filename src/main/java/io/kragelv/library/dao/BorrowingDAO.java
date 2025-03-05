package io.kragelv.library.dao;

import java.util.List;
import java.util.UUID;

import io.kragelv.library.model.Borrowing;

public interface BorrowingDAO extends GenericDAO<Borrowing> {

    List<Borrowing> getByBookId(UUID bookId);

    List<Borrowing> getByReaderId(UUID readerId);
    
}
