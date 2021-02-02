package com.leoncarraro.library_api.service;

import com.leoncarraro.library_api.dto.BookRequestDTO;
import com.leoncarraro.library_api.dto.BookResponseDTO;

public interface BookService {

    BookResponseDTO create(BookRequestDTO bookRequestDTO);

}
