package bg.softuni.Online.Book.Store.model.dto.user;

import bg.softuni.Online.Book.Store.validations.passwordMatch.PasswordMatch;
import jakarta.validation.constraints.Size;

import static bg.softuni.Online.Book.Store.constants.ValidationMessages.CONFIRM_PASSWORD_LENGTH_MESSAGE;
import static bg.softuni.Online.Book.Store.constants.ValidationMessages.PASSWORD_LENGTH_MESSAGE;

@PasswordMatch(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class ChangePasswordDTO {
    private String oldPassword;

    @Size(min = 3, max = 20, message = PASSWORD_LENGTH_MESSAGE)
    private String password;

    @Size(min = 3, max = 20, message = CONFIRM_PASSWORD_LENGTH_MESSAGE)
    private String confirmPassword;

    public ChangePasswordDTO() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public ChangePasswordDTO setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ChangePasswordDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public ChangePasswordDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
