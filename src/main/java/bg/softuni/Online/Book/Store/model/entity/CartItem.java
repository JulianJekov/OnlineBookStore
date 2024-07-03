package bg.softuni.Online.Book.Store.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

    @ManyToOne
    private Book book;

    private int quantity;

    @ManyToOne
    private ShoppingCart cart;

    public CartItem() {
    }

    public Book getBook() {
        return book;
    }

    public CartItem setBook(Book book) {
        this.book = book;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartItem setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public CartItem setCart(ShoppingCart cart) {
        this.cart = cart;
        return this;
    }
}
