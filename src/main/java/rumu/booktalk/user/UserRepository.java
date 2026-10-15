package rumu.booktalk.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * provides database operations for User entities
 * Spring Data JPA automatically implements this interface at runtime
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address
     * Used during login to retrieve the user's account
     * @param email the email address to search for
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks whether a user with the given email already exists.
     * Used during registration to prevent duplicate email addresses.
     *
     * @param email the email address to check
     * @return true if the email is already registered; false otherwise
     */
    boolean existsByEmail(String email);
}
