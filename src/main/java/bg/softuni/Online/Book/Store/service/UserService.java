package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.user.ChangePasswordDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserProfileDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserRegisterDTO;
import bg.softuni.Online.Book.Store.model.entity.User;
import org.springframework.security.core.Authentication;

public interface UserService {
    void register(UserRegisterDTO userRegisterDTO);

    User saveUser(User user);
    //todo : make it void maybe

    void updateUserLastLogin(User user);

    User findUserByEmail(String email);

    boolean isUserExist(String email);

    void createUserIfNotExist(String email, String name);

    Authentication login(String email);

    UserProfileDTO getUserDetails(Long id);

    void validateUserProfile(UserProfileDTO userProfileDTO);

    void editProfile(UserProfileDTO userProfileDTO);

    void changePassword(Long id, ChangePasswordDTO changePasswordDTO);

    void validateOldPassword(Long id, ChangePasswordDTO changePasswordDTO);
}
