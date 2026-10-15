package rumu.booktalk.wishlist;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for a wishlist entry.
 * A combination of userId and bookId uniquely identifies an entry.
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class WishListId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    /**
     * Creates a key for an existing user and book.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book
     */
    public WishListId(Long userId, Long bookId) {
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.bookId = Objects.requireNonNull(bookId, "bookId must not be null");
    }
}
