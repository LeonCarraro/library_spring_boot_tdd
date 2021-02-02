package com.leoncarraro.library_api.model;

import com.leoncarraro.library_api.dto.BookRequestDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_book")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String title;
    private String author;
    private String isbn;

    public Book(BookRequestDTO bookRequest) {
        id = null;
        title = bookRequest.getTitle();
        author = bookRequest.getAuthor();
        isbn = bookRequest.getIsbn();
    }

}
