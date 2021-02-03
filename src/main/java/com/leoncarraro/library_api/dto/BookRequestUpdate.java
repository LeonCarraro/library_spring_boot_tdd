package com.leoncarraro.library_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class BookRequestUpdate {

    @NotBlank(message = "Book Title cannot be null / empty")
    private String title;

    @NotBlank(message = "Book Author cannot be null / empty")
    private String author;

}
