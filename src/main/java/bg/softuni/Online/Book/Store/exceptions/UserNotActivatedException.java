package bg.softuni.Online.Book.Store.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {
    public UserNotActivatedException(String message) {
        super(message);
    }
}
