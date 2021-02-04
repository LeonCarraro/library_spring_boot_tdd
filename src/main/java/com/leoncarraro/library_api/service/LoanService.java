package com.leoncarraro.library_api.service;

import com.leoncarraro.library_api.dto.LoanRequestCreate;
import com.leoncarraro.library_api.dto.LoanResponse;

public interface LoanService {

    LoanResponse create(LoanRequestCreate loanRequest);

}
