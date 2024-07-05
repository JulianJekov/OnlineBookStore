package bg.softuni.Online.Book.Store.constants;

public enum ValidationMessages {
    ;
    public static final String USERNAME_LENGTH_MESSAGE = "Username must be between 3 and 20 characters!";
    public static final String USERNAME_CAN_NOT_BE_NULL = "Username cannot be null!";
    public static final String USERNAME_EXISTS_MESSAGE = "Username already exists!";

    public static final String FIRST_NAME_CAN_NOT_BE_EMPTY = "First Name must not be empty!";
    public static final String LAST_NAME_CAN_NOT_BE_EMPTY = "Last Name must not be empty!";

    public static final String PASSWORD_LENGTH_MESSAGE = "Password must be between 3 and 20 characters!";
    public static final String CONFIRM_PASSWORD_LENGTH_MESSAGE = "Confirm Password must be between 3 and 20 characters!";

    public static final String EMAIL_EXISTS_MESSAGE = "Email already exists!";
    public static final String EMAIL_CAN_NOT_BE_EMPTY = "Email must not be empty!";

    public static final String AGE_POSITIVE = "Age must be positive!";


    ValidationMessages() {
    }
}
