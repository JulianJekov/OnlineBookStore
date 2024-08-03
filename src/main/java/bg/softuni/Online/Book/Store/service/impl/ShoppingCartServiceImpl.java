package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.cartItem.CartItemDTO;
import bg.softuni.Online.Book.Store.model.dto.cartItem.ShoppingCartDTO;
import bg.softuni.Online.Book.Store.model.entity.*;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.repository.CartItemRepository;
import bg.softuni.Online.Book.Store.repository.ShoppingCartRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.ShoppingCartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static bg.softuni.Online.Book.Store.constants.Exceptions.*;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   UserRepository userRepository,
                                   CartItemRepository cartItemRepository,
                                   BookRepository bookRepository,
                                   ModelMapper modelMapper) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ShoppingCart getShoppingCartByUser(User user) {
        Optional<ShoppingCart> optionalShoppingCart =
                shoppingCartRepository.findByUser(user);
        return optionalShoppingCart.orElseGet(() -> createShoppingCart(user));
    }

    @Override
    public void addItemToCart(CartItemDTO cartItemDTO, BookStoreUserDetails userDetails) {
        Long userId = userDetails.getId();
        Long bookId = cartItemDTO.getBookId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format(USER_NOT_FOUND, userId)));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(
                () -> new ObjectNotFoundException(SHOPPING_CART_NOT_FOUND));

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ObjectNotFoundException(String.format(BOOK_NOT_FOUND, bookId)));

        CartItem cartItem = cartItemRepository.findByShoppingCartAndBook(shoppingCart, book);

        if (cartItem == null) {
            cartItem = modelMapper.map(cartItemDTO, CartItem.class);
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());
        }
        cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDTO viewShoppingCart(BookStoreUserDetails userDetails) {
        Long userId = userDetails.getId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format(USER_NOT_FOUND, userId)));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(
                () -> new ObjectNotFoundException(SHOPPING_CART_NOT_FOUND));

        return modelMapper.map(shoppingCart, ShoppingCartDTO.class);
    }

    @Override
    public void deleteItem(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByCartItemsId(id).orElseThrow(
                () -> new ObjectNotFoundException(SHOPPING_CART_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Cart item not found"));
        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.deleteById(id);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        user.setShoppingCart(shoppingCart);
        return saveShoppingCart(shoppingCart);
    }
}
