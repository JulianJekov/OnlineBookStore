package bg.softuni.Online.Book.Store.model.dto.user;

import bg.softuni.Online.Book.Store.validations.passwordMatch.PasswordMatch;
import bg.softuni.Online.Book.Store.validations.uniqueEmail.UniqueEmail;
import bg.softuni.Online.Book.Store.validations.uniqueUsername.UniqueUsername;
import jakarta.validation.constraints.*;

import static bg.softuni.Online.Book.Store.constants.ValidationMessages.*;

@PasswordMatch(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class UserRegisterDTO {

    @Size(min = 3, max = 20, message = USERNAME_LENGTH_MESSAGE)
    @NotNull(message = USERNAME_CAN_NOT_BE_NULL)
    @UniqueUsername
    private String username;

    @Email
    @NotBlank(message = EMAIL_CAN_NOT_BE_EMPTY)
    @UniqueEmail
    private String email;

    @NotBlank(message = FIRST_NAME_CAN_NOT_BE_EMPTY)
    private String firstName;

    @NotBlank(message = LAST_NAME_CAN_NOT_BE_EMPTY)
    private String lastName;

    @Positive(message = AGE_POSITIVE)
    @NotNull
    private int age;

    @Size(min = 3, max = 20, message = PASSWORD_LENGTH_MESSAGE)
    @NotNull
    private String password;

    @Size(min = 3, max = 20, message = CONFIRM_PASSWORD_LENGTH_MESSAGE)
    @NotNull
    private String confirmPassword;

    public UserRegisterDTO() {
    }

    public String getUsername() {
        return username;
    }

    public UserRegisterDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }


    public int getAge() {
        return age;
    }

    public UserRegisterDTO setAge(int age) {
        this.age = age;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
