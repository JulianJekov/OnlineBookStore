package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.model.entity.ShoppingCart;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.repository.ShoppingCartRepository;
import bg.softuni.Online.Book.Store.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart getShoppingCartByUser(User user) {
        Optional<ShoppingCart> optionalShoppingCart =
                shoppingCartRepository.findByUser(user);
        return optionalShoppingCart.orElseGet(() -> createShoppingCart(user));
    }

    @Override
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return saveShoppingCart(shoppingCart);
    }


}
