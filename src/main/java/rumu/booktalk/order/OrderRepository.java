package rumu.booktalk.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides database operations for customer orders.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves a user's orders, newest first, with pagination.
     *
     * @param userId the ID of the user
     * @param pageable the requested page number and page size
     * @return a page containing the user's orders and pagination metadata
     */
    Page<Order> findByUser_UserIdOrderByOrderDateDesc(
            Long userId,
            Pageable pageable
    );
}
