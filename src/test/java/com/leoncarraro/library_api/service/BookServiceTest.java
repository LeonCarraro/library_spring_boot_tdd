package com.leoncarraro.library_api.service;

import com.leoncarraro.library_api.dto.BookRequest;
import com.leoncarraro.library_api.dto.BookResponse;
import com.leoncarraro.library_api.model.Book;
import com.leoncarraro.library_api.repository.BookRepository;
import com.leoncarraro.library_api.service.exception.ExistingBookException;
import com.leoncarraro.library_api.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = {SpringExtension.class})
@ActiveProfiles(value = "test")
public class BookServiceTest {

    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName(value = "Should save a Book successfully")
    public void shouldSaveBookSuccessfully() {
        BookRequest bookRequest = BookRequest.builder()
                .title("Title").author("Author").isbn("ISBN").build();
        Book savedBook = Book.builder()
                .id(1L).title("Title").author("Author").isbn("ISBN").build();

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(savedBook);

        BookResponse bookResponse = bookService.create(bookRequest);

        Assertions.assertThat(bookResponse.getId()).isEqualTo(1L);
        Assertions.assertThat(bookResponse.getTitle()).isEqualTo("Title");
        Assertions.assertThat(bookResponse.getAuthor()).isEqualTo("Author");
        Assertions.assertThat(bookResponse.getIsbn()).isEqualTo("ISBN");
    }

    @Test
    @DisplayName(value = "Should throw an ExistingBookException when try create a Book with existing ISBN")
    public void shouldThrowAnExceptionWhenCreateBookWithExistingIsbn() {
        BookRequest bookRequest = BookRequest.builder()
                .title("Title").author("Author").isbn("ISBN").build();

        Mockito.when(bookRepository.existsByIsbn(bookRequest.getIsbn())).thenReturn(true);

        Assertions.assertThatExceptionOfType(ExistingBookException.class)
                .isThrownBy(() -> bookService.create(bookRequest))
                .withMessage("ISBN: ISBN already registered!");
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(Book.class));
    }

}
