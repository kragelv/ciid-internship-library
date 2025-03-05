package io.kragelv.library.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reader {

    private UUID id;
    private String fullname;
    private String email;
    private LocalDateTime createdAt;

    public Reader() { }

    public Reader(UUID id, String fullname, String email, LocalDateTime createdAt) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
