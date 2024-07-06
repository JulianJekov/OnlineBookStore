package bg.softuni.Online.Book.Store.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_cards")
public class ShoppingCart extends BaseEntity{

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

    public ShoppingCart() {
    }

    public User getUser() {
        return user;
    }

    public ShoppingCart setUser(User user) {
        this.user = user;
        return this;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public ShoppingCart setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        return this;
    }
}
