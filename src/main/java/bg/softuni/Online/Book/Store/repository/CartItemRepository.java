package bg.softuni.Online.Book.Store.repository;

import bg.softuni.Online.Book.Store.model.entity.Book;
import bg.softuni.Online.Book.Store.model.entity.CartItem;
import bg.softuni.Online.Book.Store.model.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByShoppingCartAndBook(ShoppingCart shoppingCart, Book book);

    void deleteByBookId(Long id);
}
