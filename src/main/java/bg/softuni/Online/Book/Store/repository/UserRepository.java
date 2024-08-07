package bg.softuni.Online.Book.Store.repository;

import bg.softuni.Online.Book.Store.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    User findByActivationToken(String token);

    List<User> findByLastLoginDateBeforeAndIsActive(LocalDate sixMonthsAgo, boolean b);
}
