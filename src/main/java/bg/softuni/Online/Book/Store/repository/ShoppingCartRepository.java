package bg.softuni.Online.Book.Store.repository;

import bg.softuni.Online.Book.Store.model.entity.ShoppingCart;
import bg.softuni.Online.Book.Store.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);

    Optional<ShoppingCart> findByCartItemsId(Long cartItemsId);
}

