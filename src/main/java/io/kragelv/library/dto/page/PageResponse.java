package io.kragelv.library.dto.page;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
        long total,
        List<T>  data
) {
    public static <T> PageResponse<T> create(Page<T> page) {
        return new PageResponse<>(page.getTotalElements(),
                page.getContent());
    }

    public static <E, T> PageResponse<T> create(Page<E> page, Function<E, T> contentElementMapper) {
        return new PageResponse<>(page.getTotalElements(),
                page.get()
                        .map(contentElementMapper)
                        .toList());
    }
}
