package com.leoncarraro.library_api.service.exception;

public class ExistingBookException extends RuntimeException {

    public ExistingBookException(String msg) {
        super(msg);
    }

}
