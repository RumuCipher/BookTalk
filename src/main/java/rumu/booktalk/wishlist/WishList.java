package rumu.booktalk.wishlist;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rumu.booktalk.book.Book;
import rumu.booktalk.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "wishlist")
public class WishList {

    @EmbeddedId
    private WishListId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
