package bg.softuni.Online.Book.Store.events;

import bg.softuni.Online.Book.Store.model.entity.ShoppingCart;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.service.ShoppingCartService;
import bg.softuni.Online.Book.Store.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterListener {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public UserRegisterListener(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @EventListener
    public void handleUserRegisterEvent(UserRegisterEvent event) {
        User user = event.getUser();
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUser(user);
        shoppingCart.setUser(user);
        shoppingCartService.saveShoppingCart(shoppingCart);

        user.setShoppingCart(shoppingCart);
        userService.saveUser(user);
    }
}
