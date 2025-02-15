package com.booktracker.service;

import com.booktracker.model.BookTracker;
import com.booktracker.model.BookStatus;
import com.booktracker.repository.BookTrackerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookTrackerServiceTest {

    @Mock
    private BookTrackerRepository repository;

    @InjectMocks
    private BookTrackerService service;

    private BookTracker tracker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tracker = BookTracker.builder()
                .id(1L)
                .bookId(100L)
                .status(BookStatus.FREE)
                .borrowedAt(null)
                .returnBy(null)
                .build();
    }

    @Test
    public void testCreateRecord() {
        Long bookId = 100L;
        when(repository.save(any(BookTracker.class))).thenReturn(tracker);
        BookTracker created = service.createRecord(bookId);
        assertNotNull(created);
        assertEquals(bookId, created.getBookId());
        assertEquals(BookStatus.FREE, created.getStatus());
        verify(repository, times(1)).save(any(BookTracker.class));
    }

    @Test
    public void testGetFreeBooks() {
        List<BookTracker> freeBooks = Arrays.asList(tracker);
        when(repository.findByStatus(BookStatus.FREE)).thenReturn(freeBooks);
        List<BookTracker> result = service.getFreeBooks();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByStatus(BookStatus.FREE);
    }

    @Test
    public void testUpdateStatus() {
        Long id = tracker.getId();
        LocalDateTime borrowedAt = LocalDateTime.now();
        LocalDateTime returnBy = borrowedAt.plusDays(7);
        BookTracker updatedTracker = BookTracker.builder()
                .id(1L)
                .bookId(100L)
                .status(BookStatus.TAKEN)
                .borrowedAt(borrowedAt)
                .returnBy(returnBy)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(tracker));
        when(repository.save(any(BookTracker.class))).thenReturn(updatedTracker);

        BookTracker result = service.updateStatus(id, BookStatus.TAKEN, borrowedAt, returnBy);
        assertNotNull(result);
        assertEquals(BookStatus.TAKEN, result.getStatus());
        assertEquals(borrowedAt, result.getBorrowedAt());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any(BookTracker.class));
    }

    @Test
    public void testDeleteRecord() {
        Long id = tracker.getId();
        service.deleteRecord(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteRecordByBookId() {
        Long bookId = tracker.getBookId();
        service.deleteRecordByBookId(bookId);
        verify(repository, times(1)).deleteByBookId(bookId);
    }

    @Test
    public void testFindByBookId() {
        Long bookId = tracker.getBookId();
        when(repository.findByBookId(bookId)).thenReturn(Optional.of(tracker));
        BookTracker result = service.findByBookId(bookId);
        assertNotNull(result);
        assertEquals(bookId, result.getBookId());
        verify(repository, times(1)).findByBookId(bookId);
    }

    @Test
    public void testFindByBookId_NotFound() {
        Long bookId = 200L;
        when(repository.findByBookId(bookId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> service.findByBookId(bookId));
        String expectedMessage = "Record not found for book id: " + bookId;
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
