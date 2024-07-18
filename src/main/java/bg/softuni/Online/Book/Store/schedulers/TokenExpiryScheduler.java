package bg.softuni.Online.Book.Store.schedulers;

import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TokenExpiryScheduler {

    private final UserRepository userRepository;

    public TokenExpiryScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void invalidateExpiredTokens() {
        List<User> users = userRepository.findAll();
        LocalDate now  = LocalDate.now();
        for (User user : users) {
            if (user.getTokenExpiryDate() != null && user.getActivationToken() != null) {
                LocalDate expiryDate = user.getTokenExpiryDate();
                if (expiryDate.isBefore(now)) {
                    user.setActivationToken(null);
                    user.setTokenExpiryDate(null);
                    userRepository.save(user);
                }
            }
        }
    }
}
