package com.leoncarraro.library_api.model;

import com.leoncarraro.library_api.dto.BookRequestCreate;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_book")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

}
