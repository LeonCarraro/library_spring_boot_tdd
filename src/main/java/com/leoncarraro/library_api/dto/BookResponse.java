package com.leoncarraro.library_api.dto;

import com.leoncarraro.library_api.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookResponse {

    private final Long id;
    private final String title;
    private final String author;
    private final String isbn;

    public BookResponse(Book book) {
        id = book.getId();
        title = book.getTitle();
        author = book.getAuthor();
        isbn = book.getIsbn();
    }

}
