package bg.softuni.Online.Book.Store.exceptions;

public class UserNotActivatedException extends RuntimeException {
    public UserNotActivatedException(String message) {
        super(message);
    }
}
