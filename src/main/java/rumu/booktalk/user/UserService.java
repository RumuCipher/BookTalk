package rumu.booktalk.user;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rumu.booktalk.exception.EmailAlreadyExistsException;
import rumu.booktalk.exception.InvalidPasswordException;
import rumu.booktalk.user.dto.RegisterRequest;
import rumu.booktalk.user.dto.UserResponse;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Handles user-related business operations, including registration.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Determines whether a database error was caused by the named
     * email uniqueness constraint, rather than another database rule.
     *
     * @param exception the database integrity exception to inspect
     * @return true only when the email constraint was violated
     */
    private boolean isEmailUniqueConstraintViolation(
            DataIntegrityViolationException exception) {

        Throwable cause = exception;

        // Spring and Hibernate may wrap the underlying SQL constraint error.
        while (cause != null) {
            if (cause instanceof ConstraintViolationException violation) {
                return "uk_users_email".equals(violation.getConstraintName());
            }

            cause = cause.getCause();
        }

        return false;
    }

    /**
     * Registers a new user with a normalized email and hashed password.
     *
     * @param request validated registration details
     * @return safe account information for the newly registered user
     * @throws EmailAlreadyExistsException if the email is already registered
     */
    @Transactional
    public UserResponse register(RegisterRequest request) {

        String normalizedEmail =
                request.email().trim().toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException(
                    "An account with this email already exists"
            );
        }

        int passwordBytes = request.password()
                .getBytes(StandardCharsets.UTF_8).length;

        if (passwordBytes > 72) {
            throw new InvalidPasswordException(
                    "Password must not exceed 72 UTF-8 bytes"
            );
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);


        /*
         * Flush immediately so a database uniqueness violation occurs
         * inside this try/catch rather than later during transaction commit.
         */
        User savedUser;

        try {
            savedUser = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {

            // Only convert a violation of the email constraint into HTTP 409.
            if (isEmailUniqueConstraintViolation(exception)) {
                throw new EmailAlreadyExistsException(
                        "An account with this email already exists"
                );
            }

            // Do not misreport unrelated database errors as duplicate emails.
            throw exception;
        }


        return new UserResponse(
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

}
