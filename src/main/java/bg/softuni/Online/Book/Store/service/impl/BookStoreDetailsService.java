package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.model.entity.Role;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;
import java.util.Set;

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
                        .map(this::mapUser)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, email)));
    }

    private UserDetails mapUser(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(mapRoles(user.getRoles()))
                .disabled(false)
                .build();
    }

    private List<SimpleGrantedAuthority> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .toList();
    }
}
