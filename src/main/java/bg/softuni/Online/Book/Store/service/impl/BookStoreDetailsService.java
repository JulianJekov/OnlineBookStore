package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.UserNotActivatedException;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.model.entity.Role;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static bg.softuni.Online.Book.Store.constants.Exceptions.USER_NOT_ACTIVE;
import static bg.softuni.Online.Book.Store.constants.Exceptions.USER_WITH_EMAIL_NOT_FOUND;

public class BookStoreDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public BookStoreDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return
                userRepository
                        .findByEmail(email)
                        .map(this::checkIfActive)
                        .map(this::mapUser)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, email)));
    }

    private User checkIfActive(User user) {
        if (!user.isActive()) {
            throw new UserNotActivatedException(USER_NOT_ACTIVE);
        }
        return user;
    }

    private UserDetails mapUser(User user) {
        return new BookStoreUserDetails(
                user.getEmail(),
                user.getPassword(),
                mapRoles(user.getRoles()),
                user.getId(),
                user.getFirstName(),
                user.getLastName());
    }

    private List<SimpleGrantedAuthority> mapRoles(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .toList();
    }
}
