
package rumu.booktalk.exception;

/** Indicates that a submitted password violates the password policy. */
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
