package rumu.booktalk.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        // Decode the Base64 secret string into raw cryptographic bytes once on startup
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.secret());

        // Generate a secure SecretKey object recognized by JJWT for HMAC-SHA algorithms
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a signed JWT token for an authenticated user.
     *
     * @param userId The unique user identifier (stored in 'sub')
     * @param role   The user's role (stored in custom claims)
     * @return A compact, URL-safe JWT string
     */
    public String generateToken(String userId, String role) {

        Date now = new Date();
        // Convert expirationSeconds into milliseconds
        long expirationMillis = jwtProperties.expirationSeconds() * 1000;
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(userId)
                .issuer(jwtProperties.issuer())
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
}
