package rumu.booktalk.exception;

/**
 * Indicates that registration was attempted with an email
 * address already associated with an account.
 */
public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
