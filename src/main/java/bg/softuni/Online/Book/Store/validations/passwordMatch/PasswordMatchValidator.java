package bg.softuni.Online.Book.Store.validations.passwordMatch;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String message;
    private String password;
    private String confirmPassword;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.password = constraintAnnotation.password();
        this.confirmPassword = constraintAnnotation.confirmPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

        Object passwordValue = beanWrapper.getPropertyValue(this.password);
        Object confirmPasswordValue = beanWrapper.getPropertyValue(this.confirmPassword);

        return passwordValue != null && passwordValue.equals(confirmPasswordValue);
    }
}
