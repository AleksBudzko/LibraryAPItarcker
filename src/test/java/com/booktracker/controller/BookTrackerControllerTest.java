package com.booktracker.controller;

import com.booktracker.model.BookTracker;
import com.booktracker.model.BookStatus;
import com.booktracker.service.BookTrackerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для BookTrackerController, используя «чистый» Mockito
 * и standaloneSetup без @MockBean.
 */
class BookTrackerControllerStandaloneTest {

    @Mock
    private BookTrackerService bookTrackerService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Инициализируем Mockito
        MockitoAnnotations.openMocks(this);

        // Создаём контроллер вручную и передаём в него замоканный сервис
        com.booktracker.controller.BookTrackerController controller = new com.booktracker.controller.BookTrackerController(bookTrackerService);

        // Настраиваем MockMvc в standalone-режиме
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Для сериализации/десериализации JSON (при необходимости)
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

        mockMvc.perform(post("/tracker/books/{bookId}", bookId))
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
        when(bookTrackerService.getFreeBooks()).thenReturn(List.of(tracker));

        mockMvc.perform(get("/tracker/books/free"))
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

        when(bookTrackerService.updateStatus(
                id,
                BookStatus.TAKEN,
                borrowedAt,
                returnBy
        )).thenReturn(updatedTracker);

        // Формируем запрос с параметрами borrowedAt и returnBy
        mockMvc.perform(put("/tracker/books/{id}", id)
                        .param("status", "TAKEN")
                        .param("borrowedAt", borrowedAt.toString())
                        .param("returnBy", returnBy.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TAKEN"));
    }

    @Test
    void testDeleteRecord() throws Exception {
        Long id = 1L;
        doNothing().when(bookTrackerService).deleteRecord(id);

        mockMvc.perform(delete("/tracker/books/{id}", id))
                .andExpect(status().isOk());
    }
}
