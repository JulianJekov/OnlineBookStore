package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/history")
    public ModelAndView orderHistory() {
        return new ModelAndView("order-history");
    }

    @PostMapping("/{cartItemId}")
    public ModelAndView order(@PathVariable("cartItemId") Long cartItemId, BookStoreUserDetails userDetails) {
        orderService.placeOrder(cartItemId, userDetails);
        return new ModelAndView("redirect:/order/history");
    }
}
