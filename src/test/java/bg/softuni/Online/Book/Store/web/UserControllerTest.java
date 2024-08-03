package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.user.ChangePasswordDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserProfileDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserRegisterDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.model.entity.Role;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.RoleRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.UserService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private GreenMail greenMail;

    private UserRegisterDTO validUserRegisterDTO;

    private BookStoreUserDetails userDetails;


    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);

        validUserRegisterDTO = createValidUserRegisterDTO();

        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role userRole = createRoleUser();
        User user = createUser(userRole);
        userDetails = createUserDetails(user);
    }


    @AfterEach
    void tearDown() {
        greenMail.stop();
        userRepository.deleteAll();
    }

    @Test
    void testRegistrationGetEndpoint() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testRegistrationPostEndpointWithValidDTO() throws Exception {
        mockMvc.perform(post("/users/register").with(csrf())
                        .flashAttr("userRegisterDTO", validUserRegisterDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/login?confirmEmail"))
                .andExpect(redirectedUrl("/users/login?confirmEmail"));

        greenMail.waitForIncomingEmail(1);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        assertEquals(1, receivedMessages.length);
        MimeMessage registrationMessage = receivedMessages[0];
        assertEquals(1, registrationMessage.getAllRecipients().length);
        assertEquals(validUserRegisterDTO.getEmail(), registrationMessage.getAllRecipients()[0].toString());
        assertEquals("Account Activation", registrationMessage.getSubject());
    }

    @Test
    void testRegistrationPostEndpointWithInvalidDTO() throws Exception {
        UserRegisterDTO invalidDTO = new UserRegisterDTO()
                .setFirstName("");
        mockMvc.perform(post("/users/register").with(csrf())
                        .flashAttr("userRegisterDTO", invalidDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeExists("userRegisterDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userRegisterDTO"));
    }

    @Test
    void testLoginGetEndpoint() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testLoginPostEndpointWithNotActiveAccount() throws Exception {
        mockMvc.perform(post("/users/login-error")
                        .with(request -> {
                            request.setAttribute("not_active", true);
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"))
                .andExpect(model().attributeExists("not_activated"))
                .andExpect(model().attribute("not_activated", true));
    }

    @Test
    void testLoginPostEndpointWithBadCredentials() throws Exception {
        mockMvc.perform(post("/users/login-error")
                        .with(request -> {
                            request.setAttribute("bad_credentials", true);
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"))
                .andExpect(model().attributeExists("bad_credentials"))
                .andExpect(model().attribute("bad_credentials", true));
    }

    @Test
    @WithMockUser(username = "username")
    void testProfileGetEndpoint() throws Exception {
        mockMvc.perform(get("/users/profile")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("/profile"))
                .andExpect(model().attributeExists("userProfileDTO"))
                .andExpect(model().attribute("userProfileDTO", hasProperty("id", is(userDetails.getId()))));
    }

    @Test
    void testProfileGetEndpointWithNotAuthenticated() throws Exception {
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }


    @Test
    void testEditProfileGetEndpoint() throws Exception {
        mockMvc.perform(get("/users/edit-profile")
                        .with(user(userDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/edit-profile"))
                .andExpect(model().attributeExists("userProfileDTO"));
    }

    @Test
    void testEditProfileWithValidData() throws Exception {
        UserProfileDTO userProfileDTO = validUserProfileDTO();

        mockMvc.perform(patch("/users/edit-profile")
                        .flashAttr("userProfileDTO", userProfileDTO)
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("/profile"));
    }

    @Test
    void testEditProfileWithInvalidData() throws Exception {
        UserProfileDTO userProfileDTO = invalidUserProfileDTO();

        mockMvc.perform(patch("/users/edit-profile")
                        .flashAttr("userProfileDTO", userProfileDTO)
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"));
    }

    @Test
    void testChangePasswordGetEndpoint() throws Exception {
        mockMvc.perform(get("/users/change-password")
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("/change-password"));
    }


    private ChangePasswordDTO createValidChangePasswordDTO() {
        return new ChangePasswordDTO()
                .setOldPassword(userDetails.getPassword())
                .setPassword("test")
                .setConfirmPassword("test");
    }

    private UserProfileDTO validUserProfileDTO() {
        return new UserProfileDTO()
                .setId(userDetails.getId())
                .setFirstName(userDetails.getFirstName())
                .setLastName(userDetails.getLastName())
                .setAge(22)
                .setEmail(userDetails.getUsername())
                .setUsername("test123");
    }

    private UserProfileDTO invalidUserProfileDTO() {
        return new UserProfileDTO()
                .setId(userDetails.getId())
                .setFirstName("")
                .setLastName("")
                .setAge(-1)
                .setEmail(userDetails.getUsername())
                .setUsername("test123");
    }

    private BookStoreUserDetails createUserDetails(User user) {
        return new BookStoreUserDetails(
                user.getEmail(),
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

    private UserRegisterDTO createValidUserRegisterDTO() {
        return new UserRegisterDTO()
                .setFirstName("Test")
                .setLastName("Test")
                .setUsername("Test Test")
                .setEmail("valid@test.com")
                .setAge(33)
                .setPassword("password")
                .setConfirmPassword("password");
    }
}