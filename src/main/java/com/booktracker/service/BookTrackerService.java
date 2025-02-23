package com.booktracker.service;

import com.booktracker.model.BookTracker;
import com.booktracker.model.BookStatus;
import com.booktracker.repository.BookTrackerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookTrackerService {
    private final BookTrackerRepository repository;

    public BookTracker createRecord(Long bookId) {
        BookTracker tracker = BookTracker.builder()
                .bookId(bookId)
                .status(BookStatus.FREE)
                .build();
        return repository.save(tracker);
    }

    public List<BookTracker> getFreeBooks() {
        return repository.findByStatus(BookStatus.FREE);
    }

    public BookTracker updateStatus(Long id, BookStatus status, LocalDateTime borrowedAt, LocalDateTime returnBy) {
        BookTracker tracker = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found for id: " + id));
        tracker.setStatus(status);
        tracker.setBorrowedAt(borrowedAt);
        tracker.setReturnBy(returnBy);
        return repository.save(tracker);
    }

    public void deleteRecord(Long id) {
        repository.deleteById(id);
    }

    public void deleteRecordByBookId(Long bookId) {
        repository.deleteByBookId(bookId);
    }

    public BookTracker findByBookId(Long bookId) {
        return repository.findByBookId(bookId)
                .orElseThrow(() -> new RuntimeException("Record not found for book id: " + bookId));
    }
}
