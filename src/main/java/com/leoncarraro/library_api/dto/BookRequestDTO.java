package com.leoncarraro.library_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookRequestDTO {

    private String title;
    private String author;
    private String isbn;

}
