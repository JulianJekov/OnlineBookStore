package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.order.OrderViewDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/history")
    public ModelAndView orderHistory(@AuthenticationPrincipal BookStoreUserDetails userDetails) {
        ModelAndView modelAndView = new ModelAndView("order-history");
        List<OrderViewDTO> orders = orderService.viewOrders(userDetails);
        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

    @PostMapping("/buy/{cartItemId}")
    public ModelAndView order(@PathVariable("cartItemId") Long cartItemId,
                              @AuthenticationPrincipal BookStoreUserDetails userDetails) {
        orderService.placeOrder(cartItemId, userDetails);
        return new ModelAndView("redirect:/order/history");
    }
}
