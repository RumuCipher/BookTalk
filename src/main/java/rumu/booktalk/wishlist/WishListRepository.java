package rumu.booktalk.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides database operations for wishlist entries.
 * WishListId represents the composite primary key (userId + bookId).
 */
public interface WishListRepository
        extends JpaRepository<WishList, WishListId> {
}
