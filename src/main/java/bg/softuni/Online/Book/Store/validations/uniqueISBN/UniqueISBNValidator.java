package bg.softuni.Online.Book.Store.validations.uniqueISBN;

import bg.softuni.Online.Book.Store.service.BookService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueISBNValidator implements ConstraintValidator<UniqueISBN, String> {

    private String message;
    private final BookService bookService;

    public UniqueISBNValidator(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void initialize(UniqueISBN constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        return bookService.findByISBN(isbn).isEmpty();
    }
}
