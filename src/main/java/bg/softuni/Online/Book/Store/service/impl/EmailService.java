package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.model.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(User user) {
        String token = user.getActivationToken();
        String activationLink = "http://localhost:8080/activate?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("Account Activation");
        message.setText("Click the following link to activate your account: " + activationLink);
        mailSender.send(message);
    }

    public void sentDeactivationWarning(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Account Deactivation Warning");
        message.setText("Your account will be deactivated if you do not log in within 7 days.");
        mailSender.send(message);
    }
}
