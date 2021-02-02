package com.leoncarraro.library_api.service.impl;

import com.leoncarraro.library_api.dto.BookRequest;
import com.leoncarraro.library_api.dto.BookResponse;
import com.leoncarraro.library_api.model.Book;
import com.leoncarraro.library_api.repository.BookRepository;
import com.leoncarraro.library_api.service.BookService;
import com.leoncarraro.library_api.service.exception.ExistingBookException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookResponse findById(Long id) {
        return null;
    }

    @Override
    public BookResponse create(BookRequest bookRequest) {
        String isbn = bookRequest.getIsbn();

        if (bookRepository.existsByIsbn(isbn)) {
            throw new ExistingBookException("ISBN: " + isbn + " already registered!");
        }

        Book book = new Book(bookRequest);
        book = bookRepository.save(book);
        return new BookResponse(book);
    }

    @Override
    public void delete(Long id) {
    }

}
