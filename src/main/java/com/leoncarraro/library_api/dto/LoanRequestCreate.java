package com.leoncarraro.library_api.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class LoanRequestCreate {

    @NotBlank(message = "Book ISBN cannot be null / empty")
    private final String isbn;

    @NotBlank(message = "Customer Name cannot be null / empty")
    private final String customer;

}
