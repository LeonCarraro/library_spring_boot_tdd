package com.leoncarraro.library_api.service;

import com.leoncarraro.library_api.dto.LoanRequestCreate;
import com.leoncarraro.library_api.dto.LoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface LoanService {

    Page<LoanResponse> findWithFilter(String isbn, String customer, PageRequest pageRequest);

    LoanResponse create(LoanRequestCreate loanRequest);

}
