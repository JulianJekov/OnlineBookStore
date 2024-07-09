package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.cartItem.CartItemDTO;
import bg.softuni.Online.Book.Store.model.dto.cartItem.ShoppingCartDTO;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.service.ShoppingCartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping()
    public ModelAndView showCart(@AuthenticationPrincipal BookStoreUserDetails userDetails) {
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.viewShoppingCart(userDetails);
        ModelAndView modelAndView = new ModelAndView("shopping-cart");
        modelAndView.addObject("shoppingCartDTO", shoppingCartDTO);
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addItem(CartItemDTO cartItemDTO, @AuthenticationPrincipal BookStoreUserDetails userDetails) {
        shoppingCartService.addItemToCart(cartItemDTO, userDetails);
        return new ModelAndView("redirect:/cart");
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteItem(@PathVariable Long id) {
        shoppingCartService.deleteItem(id);
        return new ModelAndView("redirect:/cart");
    }

}
