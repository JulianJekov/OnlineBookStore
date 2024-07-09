package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.cartItem.CartItemDTO;
import bg.softuni.Online.Book.Store.model.dto.cartItem.ShoppingCartDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.model.entity.ShoppingCart;
import bg.softuni.Online.Book.Store.model.entity.User;

public interface ShoppingCartService {

    ShoppingCart getShoppingCartByUser(User user);

    ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);

    void addItemToCart(CartItemDTO cartItemDTO, BookStoreUserDetails userDetails);

    ShoppingCartDTO viewShoppingCart(BookStoreUserDetails userDetails);

    void deleteItem(Long id);
}
