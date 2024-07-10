package bg.softuni.Online.Book.Store.model.dto.order;

import java.math.BigDecimal;

public class OrderItemViewDTO {
    private BigDecimal price;
    private int quantity;
    private String bookImageUrl;
    private String bookTitle;
    private BigDecimal totalPrice;

    public OrderItemViewDTO() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderItemViewDTO setPrice(BigDecimal price) {
        this.price = price;
        this.totalPrice = this.price.multiply(new BigDecimal(this.quantity));
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderItemViewDTO setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.price.multiply(new BigDecimal(this.quantity));
        return this;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public OrderItemViewDTO setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
        return this;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public OrderItemViewDTO setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public OrderItemViewDTO setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }
}
