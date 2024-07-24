package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.model.entity.Role;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.RoleRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private BookStoreUserDetails userDetails;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = createRoleUser();
        User user = createUser(role);
        userDetails = createUserDetails(user);
    }

    @Test
    void testGetIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testGetAboutPage() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

    @Test
    void testGetHomePage() throws Exception {
        mockMvc.perform(get("/home")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("welcome"))
                .andExpect(model().attribute("welcome", userDetails.getFullName()));
    }

    private BookStoreUserDetails createUserDetails(User user) {
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

    private User createUser(Role userRole) {
        User user = new User();
        user.setEmail("test@test.com");
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("first");
        user.setLastName("last");
        user.setRoles(List.of(userRole));
        userRepository.save(user);
        return user;
    }

    private Role createRoleUser() {
        Role userRole = new Role();
        userRole.setName(UserRole.USER);
        roleRepository.save(userRole);
        return userRole;
    }
}