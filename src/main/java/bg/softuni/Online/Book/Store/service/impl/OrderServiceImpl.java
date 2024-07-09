package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.entity.*;
import bg.softuni.Online.Book.Store.repository.CartItemRepository;
import bg.softuni.Online.Book.Store.repository.OrderItemRepository;
import bg.softuni.Online.Book.Store.repository.OrderRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static bg.softuni.Online.Book.Store.constants.Exceptions.USER_NOT_FOUND;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void placeOrder(Long cartItemId, BookStoreUserDetails userDetails) {
        Long userId = userDetails.getId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format(USER_NOT_FOUND, userId)));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        //TODO: fix ex thrown

        Book book = cartItem.getBook();

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getPrice());

        order.getOrderItems().add(orderItem);

        orderRepository.save(order);

        cartItemRepository.delete(cartItem);
    }
}
