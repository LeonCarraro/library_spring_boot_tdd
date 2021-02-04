package com.leoncarraro.library_api.repository;

import com.leoncarraro.library_api.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Test
    @DisplayName(value = "Should get a Page of Books when search by title only")
    public void shouldGetPageOfBookWhenSearchByTitleOnly() {
        entityManager.persist(Book.builder().id(null).title("Title 1").author("Author 1").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Title 2").author("Author 2").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Book 1").author("Author 3").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Book 2").author("Author 4").isbn("ISBN").build());

        Page<Book> page = bookRepository.findAllByTitleContainingAndAuthorContaining(
                "Title", "", PageRequest.of(0, 12));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(2L);
        Assertions.assertThat(page.getContent().get(0).getTitle()).isEqualTo("Title 1");
        Assertions.assertThat(page.getContent().get(1).getTitle()).isEqualTo("Title 2");
    }

    @Test
    @DisplayName(value = "Should get a Page of Books when search by author only")
    public void shouldGetPageOfBookWhenSearchByAuthorOnly() {
        entityManager.persist(Book.builder().id(null).title("Title 1").author("Author 1").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Title 2").author("Author 2").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Book 1").author("Book Author 3").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Book 2").author("Book Author 4").isbn("ISBN").build());

        Page<Book> page = bookRepository.findAllByTitleContainingAndAuthorContaining(
                "", "Book", PageRequest.of(0, 12));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(2L);
        Assertions.assertThat(page.getContent().get(0).getTitle()).isEqualTo("Book 1");
        Assertions.assertThat(page.getContent().get(1).getTitle()).isEqualTo("Book 2");
    }

    @Test
    @DisplayName(value = "Should return a empty Page of Book")
    public void shouldReturnEmptyPageOfBook() {
        entityManager.persist(Book.builder().id(null).title("Title 1").author("Author 1").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Title 2").author("Author 2").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Book 1").author("Book Author 3").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Book 2").author("Book Author 4").isbn("ISBN").build());

        Page<Book> page = bookRepository.findAllByTitleContainingAndAuthorContaining(
                "Book Title", "Book Author", PageRequest.of(0, 12));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(0L);
    }

    @Test
    @DisplayName(value = "Should return 2 pages of Book")
    public void shouldReturnTwoPagesOfBook() {
        entityManager.persist(Book.builder().id(null).title("Title 1").author("Author 1").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Title 2").author("Author 2").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Title 3").author("Author 3").isbn("ISBN").build());
        entityManager.persist(Book.builder().id(null).title("Title 4").author("Author 4").isbn("ISBN").build());

        Page<Book> page = bookRepository.findAllByTitleContainingAndAuthorContaining(
                "Title", "", PageRequest.of(0, 1));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(4L);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(4L);
    }

}
