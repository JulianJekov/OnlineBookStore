package bg.softuni.Online.Book.Store.model.dto.cartItem;

import java.math.BigDecimal;

public class CartItemViewDTO {
    private Long id;
    private String bookTitle;
    private int quantity;
    private double bookPrice;
    private double totalPrice;

    public CartItemViewDTO() {
    }

    public Long getId() {
        return id;
    }

    public CartItemViewDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public CartItemViewDTO setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartItemViewDTO setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.quantity * this.bookPrice;
        return this;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public CartItemViewDTO setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
        this.totalPrice = this.bookPrice * this.quantity;
        return this;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public CartItemViewDTO setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

}
