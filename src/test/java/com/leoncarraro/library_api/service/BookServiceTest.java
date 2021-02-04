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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
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
        Book book = Book.builder().id(id).author("Author").title("Title").isbn("ISBN").build();

        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));

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
                .isThrownBy(() -> bookService.findById(id))
                .withMessage("Book not found! ID: 1");
    }

    @Test
    @DisplayName(value = "Should delete one Book correctly")
    public void shouldDeleteOneBook() {
        Long id = 1L;

        Mockito.when(bookRepository.existsById(id)).thenReturn(true);

        bookService.delete(id);

        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName(value = "Should throw a ResourceNotFoundException when delete one Book with no existent ID")
    public void shouldThrowAnExceptionWhenDeleteOneBookWithNoExistentId() {
        Long id = 1L;

        Mockito.when(bookRepository.existsById(id)).thenReturn(false);

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> bookService.delete(id))
                .withMessage("Book not found! ID: 1");
    }

    @Test
    @DisplayName(value = "Should update one Book correctly")
    public void shouldUpdateOneBook() {
        Long id = 1L;
        Book book = Book.builder().id(id).author("Author").title("Title").isbn("ISBN").build();
        BookRequestUpdate bookRequest = BookRequestUpdate.builder()
                .author("Updated Author").title("Updated Title").build();

        Book updatedBook = Book.builder()
                .id(book.getId()).author(bookRequest.getAuthor()).title(bookRequest.getTitle()).isbn(book.getIsbn())
                .build();

        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(updatedBook);

        BookResponse bookResponse = bookService.update(id, bookRequest);

        Assertions.assertThat(bookResponse.getId()).isEqualTo(1L);
        Assertions.assertThat(bookResponse.getTitle()).isEqualTo("Updated Title");
        Assertions.assertThat(bookResponse.getAuthor()).isEqualTo("Updated Author");
        Assertions.assertThat(bookResponse.getIsbn()).isEqualTo("ISBN");
    }

    @Test
    @DisplayName(value = "Should throw a ResourceNotFoundException when update one Book with no existent ID")
    public void shouldThrowAnExceptionWhenUpdateOneBookWithNoExistentId() {
        Long id = 1L;

        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> bookService.update(id, Mockito.any(BookRequestUpdate.class)))
                .withMessage("Book not found! ID: 1");
        Mockito.verify(bookRepository, Mockito.times(0)).save(Mockito.any(Book.class));
    }

    @Test
    @DisplayName(value = "Should return a Page of BookResponse")
    public void shouldReturnPageOfBookResponse() {
        List<Book> pageContent = List.of(
                Book.builder().id(1L).title("Title").author("Author").isbn("ISBN").build());
        PageRequest pageRequest = PageRequest.of(0, 12);
        Page<Book> page = new PageImpl<>(pageContent, pageRequest, 1);

        Mockito.when(bookRepository.findAllByTitleContainingAndAuthorContaining(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<BookResponse> pageResponse = bookService.findWithFilter("Title", "Author", pageRequest);

        Assertions.assertThat(pageResponse.getTotalElements()).isEqualTo(1L);
        Assertions.assertThat(pageResponse.getTotalPages()).isEqualTo(1L);
        Assertions.assertThat(pageResponse.getPageable().getPageNumber()).isEqualTo(0L);
        Assertions.assertThat(pageResponse.getPageable().getPageSize()).isEqualTo(12L);
    }

}
