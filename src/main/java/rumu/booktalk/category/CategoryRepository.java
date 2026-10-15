package rumu.booktalk.category;

import org.springframework.data.jpa.repository.JpaRepository;

// Provides database operations for Category entities
// Standard CRUD operations are inherited from JpaRepository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
