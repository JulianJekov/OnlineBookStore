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

    public static final String AGE_POSITIVE = "Age must be greater than 0!";

    public static final String BOOK_TITLE_CAN_NOT_BE_NULL = "Book title cannot be null!";
    public static final String BOOK_TITLE_EXISTS_MESSAGE = "Book title already exists!";
    public static final String BOOK_TITLE_LENGTH_MESSAGE = "Book title must be between 2 and 50 characters!";

    public static final String BOOK_AUTHOR_CAN_NOT_BE_NULL = "Book author cannot be null!";
    public static final String BOOK_AUTHOR_LENGTH_MESSAGE = "Book author must be between 3 and 50 characters!";

    public static final String BOOK_PUBLISHER_CAN_NOT_BE_NULL = "Book publisher cannot be null!";
    public static final String BOOK_PUBLISHER_LENGTH_MESSAGE = "Book publisher must be between 3 and 50 characters!";

    public static final String BOOK_PRICE_POSITIVE = "Book price must be greater than 0!";

    public static final String IMAGE_CAN_NOT_BE_EMPTY = "You should choose an image file";
    public static final String IMAGE_MAX_SIZE = "Max size can not be more than %d";
    public static final String IMAGE_TYPE_MESSAGE =  "Invalid file type. Supported files: %s";

    public static final String BOOK_ISBN_EXISTS = "Book ISBN already exists!";
    public static final String BOOK_ISBN_SIZE = "Book ISBN must be 13 characters!";
    public static final String BOOK_ISBN_CAN_NOT_BE_NULL = "Book ISBN cannot be null!";


    ValidationMessages() {
    }
}
