package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static bg.softuni.Online.Book.Store.constants.Messages.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private static final String ACTIVATION_LINK = "http://localhost:8080/activate?token=";

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setActivationToken("testToken");
    }

    @Test
    void testSendActivationEmail() {
        emailService.sendActivationEmail(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(ACCOUNT_ACTIVATION);
        message.setText(ACCOUNT_ACTIVATION_MESSAGE + ACTIVATION_LINK + user.getActivationToken());

        verify(mailSender).send(message);
    }

    @Test
    void testSendDeactivationEmail() {
        emailService.sentDeactivationWarning(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(ACCOUNT_DEACTIVATION);
        message.setText(ACCOUNT_DEACTIVATION_MESSAGE);

        verify(mailSender).send(message);
    }

}