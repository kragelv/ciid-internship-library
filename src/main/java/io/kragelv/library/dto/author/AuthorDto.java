package io.kragelv.library.dto.author;

public record AuthorDto(
    String id,
    String firstName,
    String middleName,
    String lastName,
    Integer birthYear
) {
    
}
