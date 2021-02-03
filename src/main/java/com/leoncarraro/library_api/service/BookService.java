package com.leoncarraro.library_api.service;

import com.leoncarraro.library_api.dto.BookRequestCreate;
import com.leoncarraro.library_api.dto.BookRequestUpdate;
import com.leoncarraro.library_api.dto.BookResponse;

public interface BookService {

    BookResponse findById(Long id);
    BookResponse create(BookRequestCreate bookRequest);
    BookResponse update(Long id, BookRequestUpdate bookRequest);

    void delete(Long id);

}
