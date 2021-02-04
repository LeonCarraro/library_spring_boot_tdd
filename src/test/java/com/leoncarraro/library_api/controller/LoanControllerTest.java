package com.leoncarraro.library_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoncarraro.library_api.dto.LoanRequestCreate;
import com.leoncarraro.library_api.dto.LoanResponse;
import com.leoncarraro.library_api.service.LoanService;
import com.leoncarraro.library_api.service.exception.ResourceNotRegisteredException;
import com.leoncarraro.library_api.service.exception.BookAlreadyOnLoanException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles(value = "test")
@WebMvcTest(controllers = {LoanController.class})
@AutoConfigureMockMvc
public class LoanControllerTest {

    private static final String LOAN_URI = "/api/loans";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoanService loanService;

    @Test
    @DisplayName(value = "Should return a 201 Created status " +
            "with the Loan information on response body and the Location header when POST one Loan")
    public void shouldReturnCreatedStatus_WhenPostOneLoan() throws Exception {
        LoanRequestCreate loanRequest = LoanRequestCreate.builder().isbn("ISBN").customer("Customer").build();
        LoanResponse loanResponse = LoanResponse.builder().id(1L).isbn("ISBN").customer("Customer").build();

        Mockito.when(loanService.create(Mockito.any(LoanRequestCreate.class))).thenReturn(loanResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(LOAN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loanRequest));

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value("ISBN"))
                .andExpect(MockMvcResultMatchers.jsonPath("customer").value("Customer"))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/loans/1"));
    }

    @Test
    @DisplayName(value = "Should return a 400 Bad Request status with the errors message on response body" +
            " when POST one Loan with invalid attributes")
    public void shouldReturnBadRequestStatus_WhenPostOneLoanWithInvalidAttributes() throws Exception {
        LoanRequestCreate loanRequest = LoanRequestCreate.builder().isbn(null).customer(null).build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(LOAN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loanRequest));

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(2)));
        Mockito.verify(loanService, Mockito.times(0)).create(Mockito.any(LoanRequestCreate.class));
    }

    @Test
    @DisplayName(value = "Should return a 400 Bad Request status with the errors message on response body" +
            " when POST one Loan with the Book already on loan")
    public void shouldReturnBadRequestStatus_WhenPostOneLoanWithOneBookAlreadyOnLoan() throws Exception {
        LoanRequestCreate loanRequest = LoanRequestCreate.builder().isbn("ISBN").customer("Customer").build();

        Mockito.when(loanService.create(Mockito.any(LoanRequestCreate.class)))
                .thenThrow(new BookAlreadyOnLoanException("Book already on loan! ISBN: 'ISBN'"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(LOAN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loanRequest));

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("Book already on loan! ISBN: 'ISBN'"));
    }

    @Test
    @DisplayName(value = "Should return a 400 Bad Request status with the errors message on response body " +
            "when POST one Loan with an no existent ISBN")
    public void shouldReturnBadRequestStatus_WhenPostOneLoanWithNoExistentIsbn() throws Exception {
        LoanRequestCreate loanRequest = LoanRequestCreate.builder().isbn("ISBN").customer("Customer").build();

        Mockito.when(loanService.create(Mockito.any(LoanRequestCreate.class)))
                .thenThrow(new ResourceNotRegisteredException("Book not registered! ISBN: 'ISBN'"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(LOAN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loanRequest));

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("Book not registered! ISBN: 'ISBN'"));
    }

}
