package com.booktracker.kafka;

import com.booktracker.service.BookTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookTrackerKafkaListener {
    private final BookTrackerService service;

    @KafkaListener(topics = "book_created", groupId = "book_tracker_group")
    public void handleBookCreated(Long bookId) {
        service.createRecord(bookId);
    }

    @KafkaListener(topics = "book_deleted", groupId = "book_tracker_group")
    public void handleBookDeleted(Long bookId) {
        service.deleteRecordByBookId(bookId);
    }
}
