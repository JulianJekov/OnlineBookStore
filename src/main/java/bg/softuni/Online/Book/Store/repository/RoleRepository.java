package bg.softuni.Online.Book.Store.repository;

import bg.softuni.Online.Book.Store.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
