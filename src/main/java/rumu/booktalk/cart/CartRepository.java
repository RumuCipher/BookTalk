package rumu.booktalk.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provides database operations for shopping carts.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Finds a cart using its owner's user ID.
     *
     * @param userId the ID of the user who owns the cart
     * @return the cart if found, or empty otherwise
     */
    Optional<Cart> findByUser_UserId(Long userId);
}
