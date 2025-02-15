package com.booktracker.controller;

import com.booktracker.model.BookTracker;
import com.booktracker.model.BookStatus;
import com.booktracker.service.BookTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tracker")
@RequiredArgsConstructor
public class BookTrackerController {

    private final BookTrackerService service;

    /**
     * Создание записи для новой книги.
     * Обычно этот вызов происходит через Kafka-событие, но можно вызвать и напрямую.
     */
    @PostMapping("/books/{bookId}")
    public BookTracker createRecord(@PathVariable Long bookId) {
        return service.createRecord(bookId);
    }

    /**
     * Получение списка книг, которые свободны для чтения.
     */
    @GetMapping("/books/free")
    public List<BookTracker> getFreeBooks() {
        return service.getFreeBooks();
    }

    /**
     * Изменение статуса записи книги.
     * Параметры borrowedAt и returnBy передаются в формате ISO-8601 (например, "2025-02-16T10:15:30").
     */
    @PutMapping("/books/{id}")
    public BookTracker updateStatus(@PathVariable Long id,
                                    @RequestParam BookStatus status,
                                    @RequestParam(required = false) String borrowedAt,
                                    @RequestParam(required = false) String returnBy) {
        LocalDateTime borrowedAtTime = borrowedAt != null ? LocalDateTime.parse(borrowedAt) : null;
        LocalDateTime returnByTime = returnBy != null ? LocalDateTime.parse(returnBy) : null;
        return service.updateStatus(id, status, borrowedAtTime, returnByTime);
    }

    /**
     * Удаление записи о книге по идентификатору записи.
     */
    @DeleteMapping("/books/{id}")
    public void deleteRecord(@PathVariable Long id) {
        service.deleteRecord(id);
    }
}
