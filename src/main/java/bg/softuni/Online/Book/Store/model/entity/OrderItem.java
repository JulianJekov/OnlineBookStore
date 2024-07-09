package bg.softuni.Online.Book.Store.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @ManyToOne(optional = false)
    private Order order;

    @ManyToOne(optional = false)
    private Book book;

    private int quantity;

    private BigDecimal price;

    public OrderItem() {
    }

    public Order getOrder() {
        return order;
    }

    public OrderItem setOrder(Order order) {
        this.order = order;
        return this;
    }

    public Book getBook() {
        return book;
    }

    public OrderItem setBook(Book book) {
        this.book = book;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderItem setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderItem setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
