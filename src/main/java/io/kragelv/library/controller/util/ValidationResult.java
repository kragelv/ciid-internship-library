package io.kragelv.library.controller.util;

import java.util.List;

public record ValidationResult<T>(
        T value,
        List<String> errors) {
}
