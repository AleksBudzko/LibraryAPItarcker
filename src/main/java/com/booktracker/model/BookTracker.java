package com.booktracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_tracker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID книги из book-storage-service
    @Column(nullable = false, unique = true)
    private Long bookId;

    // Статус книги: FREE или TAKEN
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    // Время, когда книгу взяли (может быть null, если книга свободна)
    private LocalDateTime borrowedAt;

    // Время, когда книгу нужно вернуть (может быть null)
    private LocalDateTime returnBy;
}
