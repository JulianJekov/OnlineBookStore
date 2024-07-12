package bg.softuni.Online.Book.Store.model.dto.user;

public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public ChangePasswordDTO() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public ChangePasswordDTO setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public ChangePasswordDTO setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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
