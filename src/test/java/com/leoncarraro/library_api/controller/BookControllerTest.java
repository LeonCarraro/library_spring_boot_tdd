package com.leoncarraro.library_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoncarraro.library_api.dto.BookRequestDTO;
import com.leoncarraro.library_api.dto.BookResponseDTO;
import com.leoncarraro.library_api.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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
    @DisplayName(value = "Should create one Book successfully")
    public void shouldCreateOneBookSuccessfully() throws Exception {
        BookRequestDTO bookRequest = BookRequestDTO.builder()
                .title("Title").author("Author").isbn("ISBN").build();
        BookResponseDTO bookResponse = BookResponseDTO.builder()
                .id(1L).title("Title").author("Author").isbn("ISBN").build();

        BDDMockito.given(bookService.create(Mockito.any(BookRequestDTO.class))).willReturn(bookResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(bookRequest.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(bookRequest.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(bookRequest.getIsbn()))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/books/1"));
    }

}
