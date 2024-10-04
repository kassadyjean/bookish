package org.example.books.models;

import lombok.Builder;

@Builder(toBuilder = true)
public record Book(
        String title,
        String author,
        String isbn
) {
}
