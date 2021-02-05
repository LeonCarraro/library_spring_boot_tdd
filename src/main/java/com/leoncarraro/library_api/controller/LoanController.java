package com.leoncarraro.library_api.controller;

import com.leoncarraro.library_api.dto.LoanRequestCreate;
import com.leoncarraro.library_api.dto.LoanResponse;
import com.leoncarraro.library_api.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api/loans")
@AllArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<LoanResponse>> findWithFilter(
            @RequestParam(value = "isbn", defaultValue = "") String isbn,
            @RequestParam(value = "customer", defaultValue = "") String customer,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(loanService.findWithFilter(isbn, customer, pageRequest));
    }

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
