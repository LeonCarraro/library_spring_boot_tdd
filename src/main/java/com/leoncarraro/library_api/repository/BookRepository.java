package com.leoncarraro.library_api.repository;

import com.leoncarraro.library_api.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByTitleContainingAndAuthorContaining(String title, String author, Pageable pageable);

    boolean existsByIsbn(String isbn);

}
