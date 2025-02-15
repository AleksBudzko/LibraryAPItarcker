package com.booktracker.kafka;

import com.booktracker.service.BookTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookTrackerKafkaListener {

    private final BookTrackerService service;

    /**
     * Обработка события создания книги.
     * Сообщение должно содержать идентификатор книги (Long).
     */
    @KafkaListener(topics = "book_created", groupId = "book_tracker_group")
    public void handleBookCreated(Long bookId) {
        service.createRecord(bookId);
    }

    /**
     * Обработка события удаления книги.
     * Сообщение должно содержать идентификатор книги (Long).
     */
    @KafkaListener(topics = "book_deleted", groupId = "book_tracker_group")
    public void handleBookDeleted(Long bookId) {
        service.deleteRecordByBookId(bookId);
    }
}
