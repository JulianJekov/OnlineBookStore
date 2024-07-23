package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.user.UserProfileDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserRegisterDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);

        validUserRegisterDTO = new UserRegisterDTO()
                .setFirstName("Test")
                .setLastName("Test")
                .setUsername("Test Test")
                .setEmail("test@test.com")
                .setAge(33)
                .setPassword("password")
                .setConfirmPassword("password");
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
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
}