package bg.softuni.Online.Book.Store.validations.uniqueBookTitle;

import bg.softuni.Online.Book.Store.repository.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueBookTitleValidator implements ConstraintValidator<UniqueBookTitle, String> {

    private final BookRepository bookRepository;
    private String message;

    public UniqueBookTitleValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void initialize(UniqueBookTitle constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        return bookRepository.findByTitle(title).isEmpty();
    }
}
