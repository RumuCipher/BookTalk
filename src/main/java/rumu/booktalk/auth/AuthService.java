package rumu.booktalk.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import rumu.booktalk.config.JwtProperties;
import rumu.booktalk.config.JwtService;
import rumu.booktalk.user.dto.LoginRequest;
import rumu.booktalk.user.dto.LoginResponse;

import java.util.List;

/**
 * Handles authentication and login-related business operations.
 * Authenticates the supplied email and password, extracts the authenticated
 * user's ID and role, and delegates JWT creation to JwtService.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            JwtProperties jwtProperties
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * Authenticates a user and returns a signed JWT access token.
     *
     * @param request validated login credentials
     * @return access token, token type, and token lifetime
     */
    public LoginResponse login(LoginRequest request) {

        // AuthenticationManager delegates credential verification to Spring Security.
        // CustomUserDetailsService normalizes the email during account lookup.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // CustomUserDetailsService uses the database user ID as the principal name.
        String userId = authentication.getName();

        // Spring Security 7 may add extra authorities (such as FACTOR_PASSWORD)
        // that record how the user logged in. Only authorities starting with
        // "ROLE_" are application roles, so we look at those and ignore the rest.
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority != null
                        && authority.startsWith("ROLE_")
                        && authority.length() > "ROLE_".length())
                .toList();

        // Fail closed: the user must have exactly one role, and we never
        // fall back to a default role.
        if (roles.size() != 1) {
            throw new IllegalStateException(
                    "Authenticated user must have exactly one role"
            );
        }

        // Store the application role in the JWT without Spring's ROLE_ prefix.
        String role = roles.getFirst().substring("ROLE_".length());

        String accessToken = jwtService.generateToken(userId, role);

        return new LoginResponse(
                accessToken,
                jwtProperties.expirationSeconds()
        );
    }
}