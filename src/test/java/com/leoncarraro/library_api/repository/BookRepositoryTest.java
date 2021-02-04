package com.leoncarraro.library_api.repository;

import com.leoncarraro.library_api.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(value = {SpringExtension.class})
@ActiveProfiles(value = "test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName(value = "Should persist a Book correctly")
    public void shouldPersistABookCorrectly() {
        Book book = Book.builder().id(null).title("Title").author("Author").isbn("ISBN").build();

        book = bookRepository.save(book);

        Assertions.assertThat(book.getId()).isNotNull();
        Assertions.assertThat(book.getId()).isGreaterThan(0L);
    }

    @Test
    @DisplayName(value = "Should return true when there is a book in the database with the ISBN already registered")
    public void shouldReturnTrueWhenIsbnExists() {
        entityManager.persist(Book.builder().id(null).title("Title").author("Author").isbn("ISBN").build());

        boolean existingIsbn = bookRepository.existsByIsbn("ISBN");

        Assertions.assertThat(existingIsbn).isTrue();
    }

    @Test
    @DisplayName(value = "Should return false when there isn't a book in the database with the ISBN already registered")
    public void shouldReturnFalseWhenIsbnExists() {
        boolean existingIsbn = bookRepository.existsByIsbn("ISBN");

        Assertions.assertThat(existingIsbn).isFalse();
    }

    @Test
    @DisplayName(value = "Should return a Optional of Book")
    public void shouldReturnOneBook() {
        Book persistedBook = entityManager.persist(
                Book.builder().id(null).title("Title").author("Author").isbn("ISBN").build());

        Optional<Book> book = bookRepository.findById(persistedBook.getId());

        Assertions.assertThat(book).containsInstanceOf(Book.class);
        Assertions.assertThat(book.get().getId()).isEqualTo(persistedBook.getId());
    }

    @Test
    @DisplayName(value = "Should return a empty Optional")
    public void shouldReturnOneEmptyOptional() {
        Optional<Book> book = bookRepository.findById(1L);

        Assertions.assertThat(book).isEmpty();
    }

    @Test
    @DisplayName(value = "Should update one Book")
    public void shouldUpdateOneBook() {
        Book persistedBook = entityManager.persist(
                Book.builder().id(null).title("Title").author("Author").isbn("ISBN").build());
        Book updateBook = Book.builder()
                .id(persistedBook.getId()).author("Updated Author").isbn("ISBN").title("Updated Title").build();

        Book book = bookRepository.save(updateBook);

        Assertions.assertThat(book.getId()).isEqualTo(persistedBook.getId());
        Assertions.assertThat(book.getAuthor()).isEqualTo("Updated Author");
        Assertions.assertThat(book.getIsbn()).isEqualTo("ISBN");
        Assertions.assertThat(book.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @DisplayName(value = "Should delete one Book")
    public void shouldDeleteOneBook() {
        Book persistedBook = entityManager.persist(
                Book.builder().id(null).title("Title").author("Author").isbn("ISBN").build());

        bookRepository.deleteById(persistedBook.getId());

        Book deletedBook = entityManager.find(Book.class, persistedBook.getId());

        Assertions.assertThat(deletedBook).isNull();
    }

}
