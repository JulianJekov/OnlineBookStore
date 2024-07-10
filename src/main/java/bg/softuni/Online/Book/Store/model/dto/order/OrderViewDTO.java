package bg.softuni.Online.Book.Store.model.dto.order;

import java.time.LocalDate;
import java.util.List;

public class OrderViewDTO {
    private Long id;
    private LocalDate orderDate;
    private List<OrderItemViewDTO> orderItems;

    public OrderViewDTO() {
    }

    public Long getId() {
        return id;
    }

    public OrderViewDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public OrderViewDTO setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public List<OrderItemViewDTO> getOrderItems() {
        return orderItems;
    }

    public OrderViewDTO setOrderItems(List<OrderItemViewDTO> orderItems) {
        this.orderItems = orderItems;
        return this;
    }
}
