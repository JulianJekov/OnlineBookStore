package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.model.entity.Role;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.RoleRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestUtils {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public BookStoreUserDetails createUserDetails(User user) {
        return new BookStoreUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                        .toList(),
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public User createUser(Role userRole, String username, String email) {
        User user = new User()
                .setEmail(email)
                .setUsername(username)
                .setPassword("password")
                .setFirstName("first")
                .setLastName("last")
                .setRoles(List.of(userRole));
        return userRepository.save(user);
    }

    public Role createRoleUser(UserRole userRole) {
        Role role = new Role();
        role.setName(userRole);
        return roleRepository.save(role);
    }

    public void cleanUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
