package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.UserRegisterDTO;
import bg.softuni.Online.Book.Store.model.entity.User;

import java.util.Optional;

public interface UserService {
    void register(UserRegisterDTO userRegisterDTO);

    User saveUser(User user);
    //todo : make it void maybe

    void updateUserLastLogin(User user);

    User findUserByEmail(String email);
}
