package rumu.booktalk.book;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rumu.booktalk.category.Category;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "book_id")
    private Long bookId;

    @Setter
    @Column(name = "book_title",nullable = false)
    private String bookTitle;

    @Setter
    @Column(name = "book_author", nullable = false)
    private String bookAuthor;

    @Setter
    @Column(name = "description")
    private String bookDescription;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
