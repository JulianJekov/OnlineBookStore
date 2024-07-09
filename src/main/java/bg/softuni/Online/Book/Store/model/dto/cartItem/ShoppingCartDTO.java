package bg.softuni.Online.Book.Store.model.dto.cartItem;

import java.util.List;

public class ShoppingCartDTO {
    private List<CartItemViewDTO> cartItems;

    public ShoppingCartDTO() {
    }

    public List<CartItemViewDTO> getCartItems() {
        return cartItems;
    }

    public ShoppingCartDTO setCartItems(List<CartItemViewDTO> cartItems) {
        this.cartItems = cartItems;
        return this;
    }
}
