package rumu.booktalk.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rumu.booktalk.user.UserService;
import rumu.booktalk.user.dto.RegisterRequest;
import rumu.booktalk.user.dto.UserResponse;

/**
 * Exposes public authentication-related HTTP endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new customer account.
     *
     * @param request validated registration data
     * @return HTTP 201 Created with the new account's safe details
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
