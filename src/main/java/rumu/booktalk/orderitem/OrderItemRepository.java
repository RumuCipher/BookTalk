package rumu.booktalk.orderitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database operations for individual order items.
 */
public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    /**
     * Retrieves all items belonging to a specific order.
     *
     * @param orderId the ID of the order
     * @return a list of items belonging to that order
     */
    List<OrderItem> findByOrder_OrderId(Long orderId);
}
