package bg.softuni.Online.Book.Store.schedulers;

import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.impl.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class InactiveUserScheduler {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public InactiveUserScheduler(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deactivateInactiveUsers() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<User> inactiveUsers =
                userRepository.findByLastLoginDateBeforeAndIsActive(sixMonthsAgo, true);

        for (User user : inactiveUsers) {
            emailService.sentDeactivationWarning(user);

            if (user.getLastLoginDate().isBefore(LocalDate.now().minusMonths(6).minusDays(7))) {
                user.setActive(false);
                userRepository.save(user);
            }
        }

    }

}
