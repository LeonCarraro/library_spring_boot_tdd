package com.leoncarraro.library_api.service;

import com.leoncarraro.library_api.dto.BookRequestCreate;
import com.leoncarraro.library_api.dto.BookRequestUpdate;
import com.leoncarraro.library_api.dto.BookResponse;
import com.leoncarraro.library_api.model.Book;
import com.leoncarraro.library_api.repository.BookRepository;
import com.leoncarraro.library_api.service.exception.ExistingBookException;
import com.leoncarraro.library_api.service.exception.ResourceNotFoundException;
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

import java.util.Optional;

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
        BookRequestCreate bookRequest = BookRequestCreate.builder()
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
        BookRequestCreate bookRequest = BookRequestCreate.builder()
                .title("Title").author("Author").isbn("ISBN").build();

        Mockito.when(bookRepository.existsByIsbn(bookRequest.getIsbn())).thenReturn(true);

        Assertions.assertThatExceptionOfType(ExistingBookException.class)
                .isThrownBy(() -> bookService.create(bookRequest))
                .withMessage("ISBN: ISBN already registered!");
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(Book.class));
    }

    @Test
    @DisplayName(value = "Should get one Book information correctly")
    public void shouldGetOneBookInformation() {
        Long id = 1L;
        Optional<Book> book = Optional.of(Book.builder()
                .id(id).author("Author").title("Title").isbn("ISBN").build());

        Mockito.when(bookRepository.findById(id)).thenReturn(book);

        BookResponse bookResponse = bookService.findById(id);

        Assertions.assertThat(bookResponse.getId()).isEqualTo(1L);
        Assertions.assertThat(bookResponse.getTitle()).isEqualTo("Title");
        Assertions.assertThat(bookResponse.getAuthor()).isEqualTo("Author");
        Assertions.assertThat(bookResponse.getIsbn()).isEqualTo("ISBN");
    }

    @Test
    @DisplayName(value = "Should throw a ResourceNotFoundException when get one Book with no existent ID")
    public void shouldThrowAnExceptionWhenGetOneBookWithNoExistentId() {
        Long id = 1L;

        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> bookService.findById(1L))
                .withMessage("Book not found! ID: 1");
    }

}
