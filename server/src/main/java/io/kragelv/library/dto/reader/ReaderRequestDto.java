package io.kragelv.library.dto.reader;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReaderRequestDto(

    @NotBlank
    @Size(min = 3, max = 128)
    String fullname,

    @NotBlank
    @Size(min = 3, max = 128)
    String email
) {
}