package com.leoncarraro.library_api.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class BookRequestUpdate {

    @NotBlank(message = "Book Title cannot be null / empty")
    private final String title;

    @NotBlank(message = "Book Author cannot be null / empty")
    private final String author;

}
