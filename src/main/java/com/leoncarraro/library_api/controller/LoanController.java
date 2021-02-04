package com.leoncarraro.library_api.controller;

import com.leoncarraro.library_api.dto.LoanRequestCreate;
import com.leoncarraro.library_api.dto.LoanResponse;
import com.leoncarraro.library_api.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api/loans")
@AllArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LoanResponse> create(@RequestBody @Valid LoanRequestCreate loanRequest) {
        LoanResponse loanResponse = loanService.create(loanRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(loanResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(loanResponse);
    }

}
