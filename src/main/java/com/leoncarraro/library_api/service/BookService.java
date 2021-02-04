package com.leoncarraro.library_api.service;

import com.leoncarraro.library_api.dto.BookRequestCreate;
import com.leoncarraro.library_api.dto.BookRequestUpdate;
import com.leoncarraro.library_api.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface BookService {

    Page<BookResponse> findWithFilter(String title, String author, PageRequest pageRequest);

    BookResponse findById(Long id);
    BookResponse create(BookRequestCreate bookRequest);
    BookResponse update(Long id, BookRequestUpdate bookRequest);

    void delete(Long id);

}
