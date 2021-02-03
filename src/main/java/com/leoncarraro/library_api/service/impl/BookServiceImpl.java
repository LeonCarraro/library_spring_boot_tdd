package com.leoncarraro.library_api.service.impl;

import com.leoncarraro.library_api.dto.BookRequestCreate;
import com.leoncarraro.library_api.dto.BookRequestUpdate;
import com.leoncarraro.library_api.dto.BookResponse;
import com.leoncarraro.library_api.model.Book;
import com.leoncarraro.library_api.repository.BookRepository;
import com.leoncarraro.library_api.service.BookService;
import com.leoncarraro.library_api.service.exception.ExistingBookException;
import com.leoncarraro.library_api.service.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found! ID: " + id));

        return new BookResponse(book);
    }

    @Override
    public BookResponse create(BookRequestCreate bookRequest) {
        String isbn = bookRequest.getIsbn();

        if (bookRepository.existsByIsbn(isbn)) {
            throw new ExistingBookException("ISBN: " + isbn + " already registered!");
        }

        Book book = new Book(bookRequest);
        book = bookRepository.save(book);
        return new BookResponse(book);
    }

    @Override
    public BookResponse update(Long id, BookRequestUpdate bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found! ID: " + id));

        BeanUtils.copyProperties(bookRequest, book);

        book = bookRepository.save(book);
        return new BookResponse(book);
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found! ID: " + id);
        }

        bookRepository.deleteById(id);
    }

}
