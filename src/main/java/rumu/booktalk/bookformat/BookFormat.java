package rumu.booktalk.bookformat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rumu.booktalk.book.Book;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "book_formats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"book_id", "format_type"})
        }
)
public class BookFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "format_id")
    private Long formatId;

    @Setter
    @Column(name = "format_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormatType formatType;

    @Setter
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Setter
    @Column(name = "stock")
    private Integer stock;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
