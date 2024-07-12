package bg.softuni.Online.Book.Store.validations.uniqueBookTitle;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static bg.softuni.Online.Book.Store.constants.ValidationMessages.BOOK_TITLE_EXISTS_MESSAGE;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueBookTitleValidator.class)
public @interface UniqueBookTitle {

    String message() default BOOK_TITLE_EXISTS_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
