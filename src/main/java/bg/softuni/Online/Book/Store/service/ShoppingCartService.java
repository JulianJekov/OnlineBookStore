package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.entity.ShoppingCart;
import bg.softuni.Online.Book.Store.model.entity.User;

public interface ShoppingCartService {

   ShoppingCart getShoppingCartByUser(User user);
   ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);
}
