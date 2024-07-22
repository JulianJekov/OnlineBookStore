package bg.softuni.Online.Book.Store.service;

import bg.softuni.Online.Book.Store.model.dto.order.OrderViewDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;

import java.util.List;

public interface OrderService {
    void placeOrder(Long cartItemId, Long id);

    List<OrderViewDTO> viewOrders(Long id);
}
