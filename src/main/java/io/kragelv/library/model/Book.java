package io.kragelv.library.model;

import java.util.UUID;

public class Book {

    private UUID id;
    private String title;
    private UUID authorId;
    private Integer publishedYear;
    private Integer availableCopies;

    public Book() { }

    public Book(UUID id, String title, UUID authorId, Integer publishedYear, Integer availableCopies) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.publishedYear = publishedYear;
        this.availableCopies = availableCopies;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
}

