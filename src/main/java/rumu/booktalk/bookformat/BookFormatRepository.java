package rumu.booktalk.bookformat;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides database operations for book formats, including
 * their prices, format types, and inventory information.
 */
public interface BookFormatRepository
        extends JpaRepository<BookFormat, Long> {
}
