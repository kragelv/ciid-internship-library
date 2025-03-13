package io.kragelv.library.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_inspections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "inspected_at", nullable = false)
    private Instant inspectedAt;

    @Column(name = "condition", nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Column(name = "comment", length = 256)
    private String comment;

    public enum Condition {
        GOOD,
        DAMAGED,
        NEEDS_REPAIR,
        LOST,
        UNDER_REVIEW
    }
}
