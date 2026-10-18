
package rumu.booktalk.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles application-specific exceptions and customizes standard
 * Spring MVC error responses for the BookTalk REST API.
 *
 * Spring MVC exceptions not explicitly overridden here are handled
 * by ResponseEntityExceptionHandler using their appropriate statuses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /** Logs unexpected errors for diagnosis without exposing details to clients. */
    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Returns HTTP 409 Conflict when an email is already registered.
     *
     * @param exception the duplicate-email exception
     * @return a safe error message with status 409
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", exception.getMessage()));
    }

    /**
     * Returns HTTP 400 Bad Request when a password violates the password policy.
     *
     * @param exception the password-policy exception
     * @return an error message with status 400
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPasswordException(
            InvalidPasswordException exception) {

        return ResponseEntity.badRequest()
                .body(Map.of("error", exception.getMessage()));
    }

    /**
     * Customizes validation responses while prioritizing required-field errors.
     *
     * @param exception the validation exception raised for an invalid request
     * @param headers response headers supplied by Spring MVC
     * @param status the HTTP status supplied by Spring MVC
     * @param request the current web request
     * @return HTTP 400 with field-specific validation messages
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();

        // First pass: required-field errors take priority.
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            if (isNotBlankViolation(error)) {
                fieldErrors.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                );
            }
        });

        // Second pass: add other errors only for fields without a required error.
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", "Validation Failed");
        response.put("fields", fieldErrors);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Checks validation constraint codes instead of message text.
     * This avoids relying on wording that may change later.
     *
     * @param error the validation error to inspect
     * @return true if the error represents a NotBlank constraint violation
     */
    private boolean isNotBlankViolation(FieldError error) {
        String[] codes = error.getCodes();

        return codes != null && Arrays.stream(codes)
                .anyMatch(code -> code.startsWith("NotBlank"));
    }

    /**
     * Returns a consistent response when Spring cannot parse the request body.
     *
     * @param exception the unreadable-body exception
     * @param headers response headers supplied by Spring MVC
     * @param status the HTTP status supplied by Spring MVC
     * @param request the current web request
     * @return HTTP 400 with a safe message
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid or malformed request body"));
    }

    /**
     * Logs unexpected application errors and returns a generic HTTP 500 response.
     * Standard Spring MVC exceptions are handled by the superclass or their
     * more-specific handlers instead of being indiscriminately mapped to 500.
     *
     * @param exception the unexpected exception
     * @return a generic error response with status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpectedException(
            Exception exception) {

        logger.error("Unexpected error while processing request", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected server error occurred"));
    }

    /**
     * Returns a generic HTTP 401 response for failed authentication.
     * The same message is used for unknown accounts and incorrect
     *
     * passwords to avoid revealing whether an email is registered.
     *
     * @return HTTP 401 with a generic error message
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException() {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
    }
}
