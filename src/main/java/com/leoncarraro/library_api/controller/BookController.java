package com.leoncarraro.library_api.controller;

import com.leoncarraro.library_api.dto.BookRequestDTO;
import com.leoncarraro.library_api.dto.BookResponseDTO;
import com.leoncarraro.library_api.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BookResponseDTO> create(@RequestBody BookRequestDTO bookRequest) {
        BookResponseDTO bookResponse = bookService.create(bookRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(bookResponse);
    }

}
