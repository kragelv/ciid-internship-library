package io.kragelv.library.model;

import java.util.List;
import java.util.UUID;

public class Book {

    private UUID id;
    private String title;
    private UUID authorId;
    private Author author;


    private Integer publishedYear;
    private Integer availableCopies;
    private List<Genre> genres;

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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorId=" + authorId +
                ", publishedYear=" + publishedYear +
                ", availableCopies=" + availableCopies +
                '}';
    }
}
