package com.booktracker.kafka;

import com.booktracker.service.BookTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class BookTrackerKafkaListenerTest {

    @Mock
    private BookTrackerService service;

    @InjectMocks
    private BookTrackerKafkaListener listener;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testHandleBookCreated() {
        Long bookId = 100L;
        listener.handleBookCreated(bookId);
        verify(service, times(1)).createRecord(bookId);
    }

    @Test
    void testHandleBookDeleted() {
        Long bookId = 100L;
        listener.handleBookDeleted(bookId);
        verify(service, times(1)).deleteRecordByBookId(bookId);
    }
}
