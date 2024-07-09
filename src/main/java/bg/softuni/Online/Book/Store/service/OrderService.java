package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;

public interface OrderService {
    void placeOrder(Long cartItemId, BookStoreUserDetails userDetails);
}
