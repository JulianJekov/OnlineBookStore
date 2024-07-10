package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.order.OrderViewDTO;
import bg.softuni.Online.Book.Store.model.entity.*;
import bg.softuni.Online.Book.Store.repository.CartItemRepository;
import bg.softuni.Online.Book.Store.repository.OrderRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static bg.softuni.Online.Book.Store.constants.Exceptions.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            CartItemRepository cartItemRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void placeOrder(Long cartItemId, BookStoreUserDetails userDetails) {
        User user = getUser(userDetails);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(CART_ITEM_NOT_FOUND, cartItemId)));

        Book book = cartItem.getBook();
        Order order = createOrder(user);
        OrderItem orderItem = createOrderItem(order, book, cartItem);
        order.getOrderItems().add(orderItem);
        orderRepository.save(order);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<OrderViewDTO> viewOrders(BookStoreUserDetails userDetails) {
        User user = getUser(userDetails);
        return user.getOrders()
                .stream()
                .map(order -> modelMapper.map(order, OrderViewDTO.class))
                .toList();

    }

    private User getUser(BookStoreUserDetails userDetails) {
        Long userId = userDetails.getId();
        return userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format(USER_NOT_FOUND, userId)));
    }


    private static OrderItem createOrderItem(Order order, Book book, CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getPrice());
        return orderItem;
    }

    private static Order createOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        return order;
    }
}
