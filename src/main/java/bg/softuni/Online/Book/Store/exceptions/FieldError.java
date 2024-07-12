package bg.softuni.Online.Book.Store.exceptions;

public class FieldError {
    private final String fieldName;
    private final String errorMessage;

    public FieldError(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
