
package rumu.booktalk.user;

import java.util.Locale;
import java.util.Objects;

/**
 * Applies BookTalk's email normalization policy consistently.
 * Emails are trimmed and converted to lowercase before being
 * stored or used for account lookup.
 */
public final class EmailNormalizer {

    private EmailNormalizer() {
        // Utility class; instances are not needed.
    }

    /**
     * Normalizes an email address for storage and lookup.
     *
     * @param email the email address to normalize
     * @return the trimmed, lowercase email address
     * @throws NullPointerException if email is null
     */
    public static String normalize(String email) {
        return Objects.requireNonNull(email, "Email must not be null")
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}
