package com.booktracker.repository;

import com.booktracker.model.BookTracker;
import com.booktracker.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface BookTrackerRepository extends JpaRepository<BookTracker, Long> {
    Optional<BookTracker> findByBookId(Long bookId);
    List<BookTracker> findByStatus(BookStatus status);
    void deleteByBookId(Long bookId);
}
