package bg.softuni.Online.Book.Store.model.dto.user;

import bg.softuni.Online.Book.Store.validations.uniqueEmail.UniqueEmail;
import bg.softuni.Online.Book.Store.validations.uniqueUsername.UniqueUsername;
import jakarta.validation.constraints.*;

import static bg.softuni.Online.Book.Store.constants.ValidationMessages.*;

public class UserProfileDTO {

    private Long id;

    @NotBlank(message = FIRST_NAME_CAN_NOT_BE_EMPTY)
    private String firstName;

    @NotBlank(message = LAST_NAME_CAN_NOT_BE_EMPTY)
    private String lastName;

    @Size(min = 3, max = 20, message = USERNAME_LENGTH_MESSAGE)
    @NotNull(message = USERNAME_CAN_NOT_BE_NULL)
    private String username;

    @Email
    @NotBlank(message = EMAIL_CAN_NOT_BE_EMPTY)
    private String email;

    @Positive(message = AGE_POSITIVE)
    @NotNull
    private int age;

    public UserProfileDTO() {
    }

    public Long getId() {
        return id;
    }

    public UserProfileDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserProfileDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserProfileDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserProfileDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfileDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserProfileDTO setAge(int age) {
        this.age = age;
        return this;
    }
}
