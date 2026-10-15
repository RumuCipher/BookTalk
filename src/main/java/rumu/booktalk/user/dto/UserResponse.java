package rumu.booktalk.user.dto;

import rumu.booktalk.user.Role;

/**
 * Exposes safe account information after registration.
 * Credentials and internal entity relationships are never returned.
 *
 * @param userId the ID assigned to the new user
 * @param name the user's display name
 * @param email the user's normalized email address
 * @param role the role assigned by the server
 */
public record UserResponse(
        Long userId,
        String name,
        String email,
        Role role
) {
}
