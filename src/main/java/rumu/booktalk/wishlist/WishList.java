package rumu.booktalk.wishlist;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rumu.booktalk.book.Book;
import rumu.booktalk.user.User;

import java.util.Objects;

/**
 * Represents a book saved by a user in their wishlist.
 * The user ID and book ID together form this entry's primary key.
 */
@Getter
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

    /**
     * Creates a wishlist entry for an existing user and book.
     * Both entities must already have database IDs.
     *
     * @param user the user saving the book
     * @param book the book being saved
     * @throws NullPointerException if either entity or either ID is null
     */
    public WishList(User user, Book book) {
        this.user = Objects.requireNonNull(user, "User must not be null");
        this.book = Objects.requireNonNull(book, "Book must not be null");

        Long userId = Objects.requireNonNull(
                user.getUserId(), "User must be persisted first"
        );
        Long bookId = Objects.requireNonNull(
                book.getBookId(), "Book must be persisted first"
        );

        this.id = new WishListId(userId, bookId);
    }
}
