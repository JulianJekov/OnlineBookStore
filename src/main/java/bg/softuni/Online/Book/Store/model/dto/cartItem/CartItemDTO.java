package bg.softuni.Online.Book.Store.model.dto.cartItem;

public class CartItemDTO {

    private Long id;

    private Long bookId;

    private int quantity;

    public CartItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public CartItemDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBookId() {
        return bookId;
    }

    public CartItemDTO setBookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartItemDTO setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
