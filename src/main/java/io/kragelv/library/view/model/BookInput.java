package io.kragelv.library.view.model;

import java.util.List;
import java.util.UUID;

public class BookInput {
    
    private String title;
    private UUID authorId;
    private Integer publishedYear;
    private Integer availableCopies;
    private List<String> genres;


    public BookInput() { }

    public BookInput(String title, UUID authorId, Integer publishedYear, Integer availableCopies, List<String> genres) {
        this.title = title;
        this.authorId = authorId;
        this.publishedYear = publishedYear;
        this.availableCopies = availableCopies;
        this.genres = genres;
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
       this.genres = genres;
    }

}
