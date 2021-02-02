package com.leoncarraro.library_api.service;

import com.leoncarraro.library_api.dto.BookRequest;
import com.leoncarraro.library_api.dto.BookResponse;

public interface BookService {

    BookResponse findById(Long id);
    BookResponse create(BookRequest bookRequest);

    void delete(Long id);

}
