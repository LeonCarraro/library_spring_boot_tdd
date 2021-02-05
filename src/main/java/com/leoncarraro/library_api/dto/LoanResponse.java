package com.leoncarraro.library_api.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LoanResponse {

    private final Long id;
    private final String customer;
    private final LocalDate loanDate;
    private final Boolean isReturned;

    private final BookResponse book;

}
