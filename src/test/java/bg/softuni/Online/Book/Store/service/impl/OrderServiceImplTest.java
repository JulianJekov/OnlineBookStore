package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.order.OrderViewDTO;
import bg.softuni.Online.Book.Store.model.entity.*;
import bg.softuni.Online.Book.Store.repository.CartItemRepository;
import bg.softuni.Online.Book.Store.repository.OrderRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testPlaceOrderShouldThrowExceptionWhenUserIsNotFound() {
        Long userId = 1L;
        Long cartItemId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> orderService.placeOrder(userId, cartItemId));
    }

    @Test
    void testPlaceOrderShouldThrowExceptionWhenCartItemIsNotFound() {
        Long userId = 1L;
        Long cartItemId = 1L;


        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> orderService.placeOrder(userId, cartItemId));
    }

    @Test
    void testPlaceOrderShouldCreateOrder() {
        Long userId = 1L;
        Long cartItemId = 1L;

        User user = new User();
        user.setId(userId);

        CartItem cartItem = new CartItem()
                .setBook(new Book())
                .setQuantity(1)
                .setPrice(BigDecimal.valueOf(1));
        cartItem.setId(cartItemId);


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        orderService.placeOrder(cartItemId, userId);

        verify(orderRepository).save(orderCaptor.capture());
        verify(cartItemRepository).delete(cartItem);

        Order order = orderCaptor.getValue();
        assertNotNull(order);
        assertEquals(userId, order.getUser().getId());
        assertEquals(1, order.getOrderItems().size());

        OrderItem orderItem = order.getOrderItems().get(0);
        assertNotNull(orderItem);
        assertEquals(1, orderItem.getQuantity());
    }

    @Test
    void testViewOrdersReturnAllOrdersOfUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order1.setId(2L);

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        user.setOrders(orders);

        OrderViewDTO orderViewDTO1 = new OrderViewDTO();
        orderViewDTO1.setId(1L);

        OrderViewDTO orderViewDTO2 = new OrderViewDTO();
        orderViewDTO1.setId(2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(order1, OrderViewDTO.class)).thenReturn(orderViewDTO1);
        when(modelMapper.map(order2, OrderViewDTO.class)).thenReturn(orderViewDTO2);

        List<OrderViewDTO> ordersViewsDTO = orderService.viewOrders(userId);

        assertEquals(2, ordersViewsDTO.size());
        assertEquals(orderViewDTO1, ordersViewsDTO.get(0));
        assertEquals(orderViewDTO2, ordersViewsDTO.get(1));

        verify(userRepository).findById(userId);
        verify(modelMapper).map(order1, OrderViewDTO.class);
        verify(modelMapper).map(order2, OrderViewDTO.class);
    }

    @Test
    void testViewOrdersWhenUserHasNoOrders() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setOrders(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<OrderViewDTO> ordersViewsDTO = orderService.viewOrders(userId);

        assertTrue(ordersViewsDTO.isEmpty());
        verify(userRepository).findById(userId);
    }
}