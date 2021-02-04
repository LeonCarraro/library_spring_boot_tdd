package com.leoncarraro.library_api.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class BookRequestCreate {

    @NotBlank(message = "Book Title cannot be null / empty")
    private final String title;

    @NotBlank(message = "Book Author cannot be null / empty")
    private final String author;

    @NotBlank(message = "Book ISBN cannot be null / empty")
    private final String isbn;

}
