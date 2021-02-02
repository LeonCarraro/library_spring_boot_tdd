package com.leoncarraro.library_api.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StandardError {

    private final List<String> errors;

}
