package bg.softuni.Online.Book.Store.validations.imageValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static bg.softuni.Online.Book.Store.constants.ValidationMessages.*;

public class ImageFileAnnotationValidation implements ConstraintValidator<ImageFileAnnotation, MultipartFile> {

    private List<String> contentTypes;
    private long size;

    @Override
    public void initialize(ImageFileAnnotation constraintAnnotation) {

        this.size = constraintAnnotation.size();
        this.contentTypes = Arrays.stream(constraintAnnotation.contentTypes()).toList();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {

        String errorMessage = getErrorMsg(multipartFile);
        if (!errorMessage.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }

        return true;
    }

    private String getErrorMsg(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return IMAGE_CAN_NOT_BE_EMPTY;
        }

        if (multipartFile.getSize() > size) {
            return String.format(IMAGE_MAX_SIZE, size);
        }

        if (!contentTypes.contains(multipartFile.getContentType())) {
            return String.format(IMAGE_TYPE_MESSAGE, String.join(", ", contentTypes));
        }

        return "";
    }
}
