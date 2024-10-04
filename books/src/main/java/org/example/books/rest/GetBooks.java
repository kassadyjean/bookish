package org.example.books.rest;

import org.example.books.models.BookResults;
import org.example.books.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetBooks {

    private final BooksService booksService;

    @Autowired
    public GetBooks(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/book/{title}")
    public ResponseEntity<BookResults> getBookResults(@PathVariable("title") String title, @RequestParam(value = "author", required = false) String author) {
        try {
             return new ResponseEntity<>(booksService.getBookResults(title, author), HttpStatus.OK);
        } catch (Exception e) {
            var message = "Failed to get books for title: %s author: %s with error %s".formatted(title, author, e.getMessage());
            System.out.println(message);

            return new ResponseEntity<>(BookResults.builder().build(), HttpStatus.I_AM_A_TEAPOT);
        }
    }

}
