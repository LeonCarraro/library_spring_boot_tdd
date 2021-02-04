package com.leoncarraro.library_api.model;

import com.leoncarraro.library_api.dto.BookRequestCreate;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_book")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;

    public Book(BookRequestCreate bookRequest) {
        id = null;
        title = bookRequest.getTitle();
        author = bookRequest.getAuthor();
        isbn = bookRequest.getIsbn();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return getId().equals(book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
