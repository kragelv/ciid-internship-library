package io.kragelv.library.dto.author;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequestDto(

        @Size(max = 64)
        String firstName,

        @Size(max = 64)
        String middleName,

        @Size(max = 64)
        @NotBlank
        String lastName,

        @Min(0)
        Integer birthYear) {

}
