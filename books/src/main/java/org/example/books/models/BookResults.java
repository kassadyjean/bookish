package org.example.books.models;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record BookResults(List<Book> books) {
}
