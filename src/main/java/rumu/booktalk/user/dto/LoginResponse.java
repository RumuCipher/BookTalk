package rumu.booktalk.user.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
    public LoginResponse(String accessToken, long expiresInSeconds) {
        this(accessToken, "Bearer", expiresInSeconds);
    }
}
