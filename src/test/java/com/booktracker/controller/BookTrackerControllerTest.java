package com.booktracker.controller;

import com.booktracker.model.BookTracker;
import com.booktracker.model.BookStatus;
import com.booktracker.service.BookTrackerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookTrackerControllerTest {

    @Mock
    private BookTrackerService bookTrackerService;

    @InjectMocks
    private BookTrackerController bookTrackerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookTrackerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateRecord() throws Exception {
        Long bookId = 100L;
        BookTracker tracker = BookTracker.builder()
                .id(1L)
                .bookId(bookId)
                .status(BookStatus.FREE)
                .build();
        when(bookTrackerService.createRecord(bookId)).thenReturn(tracker);

        mockMvc.perform(post("/tracker/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.status").value("FREE"));
    }

    @Test
    void testGetFreeBooks() throws Exception {
        BookTracker tracker = BookTracker.builder()
                .id(1L)
                .bookId(100L)
                .status(BookStatus.FREE)
                .build();
        when(bookTrackerService.getFreeBooks()).thenReturn(Collections.singletonList(tracker));

        mockMvc.perform(get("/tracker/books/free")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(100L))
                .andExpect(jsonPath("$[0].status").value("FREE"));
    }

    @Test
    void testUpdateStatus() throws Exception {
        Long id = 1L;
        LocalDateTime borrowedAt = LocalDateTime.now();
        LocalDateTime returnBy = borrowedAt.plusDays(7);
        BookTracker updatedTracker = BookTracker.builder()
                .id(id)
                .bookId(100L)
                .status(BookStatus.TAKEN)
                .borrowedAt(borrowedAt)
                .returnBy(returnBy)
                .build();
        when(bookTrackerService.updateStatus(id, BookStatus.TAKEN, borrowedAt, returnBy))
                .thenReturn(updatedTracker);

        mockMvc.perform(put("/tracker/books/{id}", id)
                        .param("status", "TAKEN")
                        .param("borrowedAt", borrowedAt.toString())
                        .param("returnBy", returnBy.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TAKEN"))
                .andExpect(jsonPath("$.borrowedAt").value(borrowedAt.toString()))
                .andExpect(jsonPath("$.returnBy").value(returnBy.toString()));
    }

    @Test
    void testDeleteRecord() throws Exception {
        Long id = 1L;
        doNothing().when(bookTrackerService).deleteRecord(id);

        mockMvc.perform(delete("/tracker/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
