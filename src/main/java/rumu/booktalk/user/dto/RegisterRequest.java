package rumu.booktalk.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

/**
 * Carries and validates the information a customer submits
 * when registering a BookTalk account.
 */
public record RegisterRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 254, message = "Email must not exceed 254 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be between 8 to 72 characters")
        String password
) {
        @Override
        @NonNull
        public String toString() {
                return "RegisterRequest[name=" + name
                        + ", email=" + email
                        + ", password=[REDACTED]]";
        }
}
