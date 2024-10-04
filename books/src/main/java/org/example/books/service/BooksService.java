package org.example.books.service;

import com.google.api.services.books.v1.model.Metadata;
import org.example.books.clients.GoogleBooksClient;
import org.example.books.models.Book;
import org.example.books.models.BookResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class BooksService {
    private final GoogleBooksClient googleBooksClient;

    @Autowired
    public BooksService(GoogleBooksClient googleBooksClient) {
        this.googleBooksClient = googleBooksClient;
    }

    public BookResults getBookResults(String title, String author) {
        var metadata = googleBooksClient.getBooks(title, author);
        var items = getItems(metadata);

        var books = Optional.ofNullable(items)
                .orElse(List.of())
                .stream()
                .map(this::getBookFromVolume)
                .toList();

        return BookResults.builder()
                .books(books)
                .build();
    }

    @SuppressWarnings(value = "unchecked")
    private List<LinkedHashMap<String, Object>> getItems(Metadata metadata) {
        return (List<LinkedHashMap<String, Object>>) metadata.get("items");
    }

    private Book getBookFromVolume(LinkedHashMap<String, Object> volume) {
        var volumeInfo = getVolumeInfo(volume);
        var authors = getAuthors(volumeInfo);
        var author = getAuthor(authors);
        var title = getTitle(volumeInfo);
        var industryIdentifiers = getIndustryIdentifiers(volumeInfo);

        return Book.builder()
                .author(author)
                .title(title)
                .isbn(getIsbn(industryIdentifiers))
                .build();
    }

    @SuppressWarnings(value = "unchecked")
    private LinkedHashMap<String, Object> getVolumeInfo(LinkedHashMap<String, Object> volume) {
        return (LinkedHashMap<String, Object>) volume.get("volumeInfo");
    }

    @SuppressWarnings(value = "unchecked")
    private List<String> getAuthors(LinkedHashMap<String, Object> volumeInfo) {
        return (List<String>) volumeInfo.get("authors");
    }

    private String getAuthor(List<String> authors) {
        return (authors != null && !authors.isEmpty()) ? authors.getFirst() : "Unknown Author";
    }

    private String getTitle(LinkedHashMap<String, Object> volumeInfo) {
        return (String) volumeInfo.getOrDefault("title", "Unknown Title");
    }

    @SuppressWarnings(value = "unchecked")
    private List<LinkedHashMap<String, String>> getIndustryIdentifiers(LinkedHashMap<String, Object> volumeInfo) {
        return (List<LinkedHashMap<String, String>>) volumeInfo.get("industryIdentifiers");
    }

    private String getIsbn(List<LinkedHashMap<String, String>> industryIdentifiers) {
        var isbn = "Unknown ISBN";

        if (industryIdentifiers != null && !industryIdentifiers.isEmpty()) {
            var identifierMap = industryIdentifiers.getFirst();
            isbn = identifierMap.getOrDefault("identifier", "Unknown ISBN");
        }

        return isbn;
    }

}
