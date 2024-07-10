package bg.softuni.Online.Book.Store.model.dto.cartItem;

public class CartItemViewDTO {
    private Long id;
    private String bookTitle;
    private String bookImageUrl;
    private int quantity;
    private double price;
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
        this.totalPrice = this.quantity * this.price;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public CartItemViewDTO setPrice(double price) {
        this.price = price;
        this.totalPrice = this.price * this.quantity;
        return this;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public CartItemViewDTO setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public CartItemViewDTO setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
        return this;
    }
}
