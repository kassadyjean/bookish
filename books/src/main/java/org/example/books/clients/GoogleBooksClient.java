package org.example.books.clients;

import com.google.api.services.books.v1.model.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleBooksClient {
    private final RestTemplate restTemplate;
    private static final String API_KEY = "MY_API_KEY";
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=%s+inauthor:%s&key=%s";

    @Autowired
    public GoogleBooksClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Metadata getBooks(String title, String author) {
        var url = GOOGLE_BOOKS_API_URL.formatted(title, author, API_KEY);

        return restTemplate.getForObject(url, Metadata.class);
    }

}
