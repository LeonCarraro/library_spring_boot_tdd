package com.leoncarraro.library_api.controller.exception;

import com.leoncarraro.library_api.service.exception.ExistingBookException;
import com.leoncarraro.library_api.service.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<StandardError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new StandardError(errors));
    }

    @ExceptionHandler(value = {ExistingBookException.class})
    public ResponseEntity<StandardError> existingBookExceptionHandler(ExistingBookException ex) {
        List<String> errors = List.of(ex.getMessage());
        return ResponseEntity.badRequest().body(new StandardError(errors));
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<StandardError> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        List<String> errors = List.of(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandardError(errors));
    }

}
