package bg.softuni.Online.Book.Store.repository;

import bg.softuni.Online.Book.Store.model.entity.Role;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
   Role findByName(UserRole userRole);
}
