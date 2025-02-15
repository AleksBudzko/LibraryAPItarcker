package com.booktracker.kafka;

import com.booktracker.service.BookTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BookTrackerKafkaListenerTest {

    @Mock
    private BookTrackerService service;

    @InjectMocks
    private BookTrackerKafkaListener listener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleBookCreated() {
        Long bookId = 100L;
        listener.handleBookCreated(bookId);
        verify(service, times(1)).createRecord(bookId);
    }

    @Test
    public void testHandleBookDeleted() {
        Long bookId = 100L;
        listener.handleBookDeleted(bookId);
        verify(service, times(1)).deleteRecordByBookId(bookId);
    }
}
