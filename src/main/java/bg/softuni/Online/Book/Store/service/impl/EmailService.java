package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.model.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static bg.softuni.Online.Book.Store.constants.Messages.*;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private static final String ACTIVATION_LINK = "https://bookstore-bookstore.azuremicroservices.io/activate?token=";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(User user) {
        String token = user.getActivationToken();
        String activationLink = ACTIVATION_LINK + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject(ACCOUNT_ACTIVATION);
        message.setText(ACCOUNT_ACTIVATION_MESSAGE + activationLink);
        mailSender.send(message);
    }

    public void sentDeactivationWarning(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(ACCOUNT_DEACTIVATION);
        message.setText(ACCOUNT_DEACTIVATION_MESSAGE);
        mailSender.send(message);
    }
}
