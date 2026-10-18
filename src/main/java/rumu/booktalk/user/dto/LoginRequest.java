package rumu.booktalk.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.NonNull;

public record LoginRequest(
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be a well-formed email address")
        String email,

        @NotBlank(message = "Password must not be blank")
        String password
) {
        @Override
        @NonNull
        public String toString() {
                return "LoginRequest[email=" + email
                        + ", password=[REDACTED]]";
        }
}
