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

    @Column(nullable = false, unique = true)
    private Long bookId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    private LocalDateTime borrowedAt;
    private LocalDateTime returnBy;
}
