package bg.softuni.Online.Book.Store.validations.imageValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileAnnotationValidation.class)
public @interface ImageFileAnnotation {

    long size() default 5 * 1024 * 1024;

    String[] contentTypes();

    String message() default "Must be valid Image file!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
