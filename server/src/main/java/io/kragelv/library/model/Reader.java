package io.kragelv.library.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "readers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "fullname", nullable = false, length = 128)
    private String fullname;

    @Column(name = "email", unique = true, nullable = false, length = 128)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "reader")
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<Borrowing> borrowings = new HashSet<>();
}
