package bg.softuni.Online.Book.Store.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    public Order() {
    }

    public User getUser() {
        return user;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Order setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }
}
