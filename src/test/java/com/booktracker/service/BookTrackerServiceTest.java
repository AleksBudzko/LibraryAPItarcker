package com.booktracker.service;

import com.booktracker.model.BookTracker;
import com.booktracker.model.BookStatus;
import com.booktracker.repository.BookTrackerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookTrackerServiceTest {

    @Mock
    private BookTrackerRepository repository;

    @InjectMocks
    private BookTrackerService service;

    private BookTracker sampleTracker;

    @BeforeEach
    void setUp() {
        sampleTracker = BookTracker.builder()
                .id(1L)
                .bookId(100L)
                .status(BookStatus.FREE)
                .borrowedAt(null)
                .returnBy(null)
                .build();
    }

    @Test
    void testCreateRecord() {
        when(repository.save(any(BookTracker.class))).thenReturn(sampleTracker);
        BookTracker created = service.createRecord(100L);
        assertNotNull(created);
        assertEquals(100L, created.getBookId());
        assertEquals(BookStatus.FREE, created.getStatus());
        verify(repository, times(1)).save(any(BookTracker.class));
    }

    @Test
    void testGetFreeBooks() {
        when(repository.findByStatus(BookStatus.FREE)).thenReturn(Collections.singletonList(sampleTracker));
        List<BookTracker> freeBooks = service.getFreeBooks();
        assertNotNull(freeBooks);
        assertEquals(1, freeBooks.size());
        assertEquals(100L, freeBooks.get(0).getBookId());
    }

    @Test
    void testUpdateStatus() {
        LocalDateTime borrowedAt = LocalDateTime.now();
        LocalDateTime returnBy = borrowedAt.plusDays(7);
        when(repository.findById(1L)).thenReturn(Optional.of(sampleTracker));

        sampleTracker.setStatus(BookStatus.TAKEN);
        sampleTracker.setBorrowedAt(borrowedAt);
        sampleTracker.setReturnBy(returnBy);
        when(repository.save(any(BookTracker.class))).thenReturn(sampleTracker);

        BookTracker updated = service.updateStatus(1L, BookStatus.TAKEN, borrowedAt, returnBy);
        assertNotNull(updated);
        assertEquals(BookStatus.TAKEN, updated.getStatus());
        assertEquals(borrowedAt, updated.getBorrowedAt());
        assertEquals(returnBy, updated.getReturnBy());
    }

    @Test
    void testDeleteRecord() {
        doNothing().when(repository).deleteById(1L);
        service.deleteRecord(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRecordByBookId() {
        doNothing().when(repository).deleteByBookId(100L);
        service.deleteRecordByBookId(100L);
        verify(repository, times(1)).deleteByBookId(100L);
    }

    @Test
    void testFindByBookId() {
        when(repository.findByBookId(100L)).thenReturn(Optional.of(sampleTracker));
        BookTracker found = service.findByBookId(100L);
        assertNotNull(found);
        assertEquals(100L, found.getBookId());
    }

    @Test
    void testFindByBookId_NotFound() {
        when(repository.findByBookId(200L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> service.findByBookId(200L));
        assertTrue(exception.getMessage().contains("Record not found for book id: 200"));
    }
}
