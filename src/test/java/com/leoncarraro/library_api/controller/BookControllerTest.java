package com.leoncarraro.library_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoncarraro.library_api.dto.BookRequest;
import com.leoncarraro.library_api.dto.BookResponse;
import com.leoncarraro.library_api.service.BookService;
import com.leoncarraro.library_api.service.exception.ExistingBookException;
import com.leoncarraro.library_api.service.exception.ResourceNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(value = {SpringExtension.class})
@ActiveProfiles(value = "test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    private static final String BOOK_URI = "/api/books";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName(value = "Should return Created status with response body and correct Location header")
    public void shouldReturnCreatedStatus_WhenSaveValidBook() throws Exception {
        BookRequest bookRequest = BookRequest.builder()
                .title("Title").author("Author").isbn("ISBN").build();
        BookResponse bookResponse = BookResponse.builder()
                .id(1L).title("Title").author("Author").isbn("ISBN").build();

        Mockito.when(bookService.create(Mockito.any(BookRequest.class))).thenReturn(bookResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value("Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value("Author"))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value("ISBN"))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/books/1"));
    }

    @Test
    @DisplayName(value = "Should throw a MethodArgumentNotValidException with errors message " +
            "when try to create one Book with invalid properties")
    public void shouldThrowAnException_WhenCreateBookWithInvalidProperties() throws Exception {
        BookRequest bookRequest = BookRequest.builder()
                .title(null).author(null).isbn(null).build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName(value = "Should return a Bad Request status with error message " +
            "when try to create a Book with existing ISBN")
    public void shouldReturnBadRequestStatus_WhenSaveBookWithExistingIsbn() throws Exception {
        BookRequest bookRequest = BookRequest.builder()
                .title("Title").author("Author").isbn("ISBN").build();

        Mockito.when(bookService.create(Mockito.any(BookRequest.class)))
                .thenThrow(new ExistingBookException("ISBN: ISBN already registered!"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("ISBN: ISBN already registered!"));
    }

    @Test
    @DisplayName(value = "Should return a Ok status with response body when get a Book information correctly")
    public void shouldReturnOkStatus_WhenGetBookInformation() throws Exception {
        BookResponse bookResponse = BookResponse.builder()
                .id(1L).author("Author").title("Title").isbn("ISBN").build();

        Mockito.when(bookService.findById(1L)).thenReturn(bookResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BOOK_URI + "/1")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value("Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value("Author"))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value("ISBN"));
    }

    @Test
    @DisplayName(value = "Should return a Not Found status with error message " +
            "when get a Book information with no existent ID")
    public void shouldReturnNotFoundStatus_WhenGetBookInformationWithNoExistentId() throws Exception {
        Mockito.when(bookService.findById(1L)).thenThrow(new ResourceNotFoundException("Book 1 not found!"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BOOK_URI + "/1")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("Book 1 not found!"));
    }

    @Test
    @DisplayName(value = "Should return a No Content status when delete a Book correctly")
    public void shouldReturnNoContentStatus_WhenDeleteBook() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(BOOK_URI + "/1");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName(value = "Should return a Not Found status with error message" +
            " when delete a Book with no existent ID")
    public void shouldReturnNotFoundStatus_WhenDeleteBookWithNoExistentId() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Book 1 not found!")).when(bookService).delete(1L);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(BOOK_URI + "/1");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("Book 1 not found!"));
    }

}
