package rumu.booktalk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Base64;

/**
 * Externalized JWT settings bound from the {@code security.jwt} prefix.
 * Validated during startup so missing or unsafe configuration fails early.
 *
 * @param secret Base64-encoded HMAC key that decodes to at least 32 bytes
 * @param expirationSeconds access-token lifetime in seconds
 * @param issuer issuer written to the JWT's {@code iss} claim
 */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secret,
        long expirationSeconds,
        String issuer
) {

    public JwtProperties {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT secret must not be blank"
            );
        }

        byte[] decodedSecret;

        // Catch only errors produced by decoding invalid Base64.
        try {
            decodedSecret = Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "JWT secret must be valid Base64",
                    exception
            );
        }

        // Validate the decoded key outside the try/catch.
        if (decodedSecret.length < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must decode to at least 32 bytes"
            );
        }

        if (expirationSeconds <= 0) {
            throw new IllegalArgumentException(
                    "JWT expiration must be positive"
            );
        }

        if (issuer == null || issuer.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT issuer must not be blank"
            );
        }
    }
}
