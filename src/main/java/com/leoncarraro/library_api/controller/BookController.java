package com.leoncarraro.library_api.controller;

import com.leoncarraro.library_api.dto.BookRequestDTO;
import com.leoncarraro.library_api.dto.BookResponseDTO;
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
    public ResponseEntity<BookResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BookResponseDTO> create(@RequestBody @Valid BookRequestDTO bookRequest) {
        BookResponseDTO bookResponse = bookService.create(bookRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(bookResponse);
    }

}
