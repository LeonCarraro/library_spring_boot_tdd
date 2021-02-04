package com.leoncarraro.library_api.service.exception;

public class BookAlreadyOnLoanException extends RuntimeException {

    public BookAlreadyOnLoanException(String msg) {
        super(msg);
    }

}
