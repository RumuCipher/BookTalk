package rumu.booktalk.cartitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provides database operations for cart items, including
 * checking whether a particular book format is already in a cart.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Finds an existing cart item by its cart ID and book-format ID.
     * Used to check whether a format is already in the cart before
     * adding it again or increasing its quantity.
     *
     * @param cartId the ID of the cart
     * @param formatId the ID of the book format
     * @return the matching cart item, or empty if none exists
     */
    Optional<CartItem> findByCart_CartIdAndFormat_FormatId(
            Long cartId,
            Long formatId
    );
}
