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

        Assertions.assertThat(book.getId()).isEqualTo(1L);
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

}
