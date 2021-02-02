package com.leoncarraro.library_api.service.impl;

import com.leoncarraro.library_api.dto.BookRequestDTO;
import com.leoncarraro.library_api.dto.BookResponseDTO;
import com.leoncarraro.library_api.model.Book;
import com.leoncarraro.library_api.repository.BookRepository;
import com.leoncarraro.library_api.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookResponseDTO create(BookRequestDTO bookRequest) {
        Book book = new Book(bookRequest);
        book = bookRepository.save(book);

        return BookResponseDTO.builder()
                .id(book.getId()).title(book.getTitle()).author(book.getAuthor()).isbn(book.getIsbn()).build();
    }

}
