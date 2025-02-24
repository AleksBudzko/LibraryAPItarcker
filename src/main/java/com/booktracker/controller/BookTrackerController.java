package com.booktracker.controller;

import com.booktracker.model.BookTracker;
import com.booktracker.model.BookStatus;
import com.booktracker.service.BookTrackerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tracker")
@RequiredArgsConstructor
public class BookTrackerController {
    private final BookTrackerService service;

    @Operation(summary = "Создать запись для новой книги")
    @PostMapping("/books/{bookId}")
    public ResponseEntity<BookTracker> createRecord(@PathVariable Long bookId) {
        return ResponseEntity.ok(service.createRecord(bookId));
    }

    @Operation(summary = "Получить список свободных книг")
    @GetMapping("/books/free")
    public ResponseEntity<List<BookTracker>> getFreeBooks() {
        return ResponseEntity.ok(service.getFreeBooks());
    }

    @Operation(summary = "Обновить статус записи книги")
    @PutMapping("/books/{id}")
    public ResponseEntity<BookTracker> updateStatus(@PathVariable Long id,
                                                    @RequestParam BookStatus status,
                                                    @RequestParam(required = false) String borrowedAt,
                                                    @RequestParam(required = false) String returnBy) {
        LocalDateTime borrowedAtTime = (borrowedAt != null) ? LocalDateTime.parse(borrowedAt) : null;
        LocalDateTime returnByTime = (returnBy != null) ? LocalDateTime.parse(returnBy) : null;
        return ResponseEntity.ok(service.updateStatus(id, status, borrowedAtTime, returnByTime));
    }

    @Operation(summary = "Удалить запись о книге")
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        service.deleteRecord(id);
        return ResponseEntity.ok().build();
    }
}
