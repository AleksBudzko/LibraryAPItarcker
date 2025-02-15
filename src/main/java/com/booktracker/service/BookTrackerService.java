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

    /**
     * Создание записи для новой книги с начальными значениями.
     * По умолчанию статус - FREE, даты взятия/возврата отсутствуют.
     */
    public BookTracker createRecord(Long bookId) {
        // Если запись для данной книги уже существует, можно её вернуть или обновить.
        // Здесь предполагается создание новой записи.
        BookTracker tracker = BookTracker.builder()
                .bookId(bookId)
                .status(BookStatus.FREE)
                .borrowedAt(null)
                .returnBy(null)
                .build();
        return repository.save(tracker);
    }

    /**
     * Получить список книг со статусом FREE.
     */
    public List<BookTracker> getFreeBooks() {
        return repository.findByStatus(BookStatus.FREE);
    }

    /**
     * Изменение статуса записи книги.
     * @param id идентификатор записи (BookTracker)
     * @param status новый статус (FREE или TAKEN)
     * @param borrowedAt время взятия книги (если применимо)
     * @param returnBy время возврата книги (если применимо)
     */
    public BookTracker updateStatus(Long id, BookStatus status, LocalDateTime borrowedAt, LocalDateTime returnBy) {
        BookTracker tracker = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found for id: " + id));
        tracker.setStatus(status);
        tracker.setBorrowedAt(borrowedAt);
        tracker.setReturnBy(returnBy);
        return repository.save(tracker);
    }

    /**
     * Удаление записи по идентификатору записи.
     */
    public void deleteRecord(Long id) {
        repository.deleteById(id);
    }

    /**
     * Удаление записи по ID книги.
     */
    public void deleteRecordByBookId(Long bookId) {
        repository.deleteByBookId(bookId);
    }

    /**
     * Поиск записи по ID книги.
     */
    public BookTracker findByBookId(Long bookId) {
        return repository.findByBookId(bookId)
                .orElseThrow(() -> new RuntimeException("Record not found for book id: " + bookId));
    }
}
