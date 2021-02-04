package com.leoncarraro.library_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanResponse {

    private final Long id;
    private final String isbn;
    private final String customer;

}
