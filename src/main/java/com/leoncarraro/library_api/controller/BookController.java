package com.leoncarraro.library_api.controller;

import com.leoncarraro.library_api.dto.BookRequestCreate;
import com.leoncarraro.library_api.dto.BookRequestUpdate;
import com.leoncarraro.library_api.dto.BookResponse;
import com.leoncarraro.library_api.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BookResponse> create(@RequestBody @Valid BookRequestCreate bookRequest) {
        BookResponse bookResponse = bookService.create(bookRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(bookResponse);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid BookRequestUpdate bookRequest) {
        return ResponseEntity.ok(bookService.update(id, bookRequest));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
