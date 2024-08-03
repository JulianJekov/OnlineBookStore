package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.cartItem.CartItemDTO;
import bg.softuni.Online.Book.Store.model.dto.cartItem.ShoppingCartDTO;
import bg.softuni.Online.Book.Store.model.entity.*;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.repository.ShoppingCartRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.ShoppingCartService;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private User user;
    private BookStoreUserDetails userDetails;
    private CartItemDTO cartItemDTO;



    @BeforeEach
    void setUp() {
        testUtils.cleanUp();

        Role roleUser = testUtils.createRoleUser(UserRole.USER);
        user = testUtils.createUser(roleUser, "testUser", "testUser@test.com");
        userDetails = testUtils.createUserDetails(user);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);

        Book book = createBook();

        cartItemDTO = new CartItemDTO();

        cartItemDTO.setBookId(book.getId());
        cartItemDTO.setQuantity(1);
    }

    @AfterEach
    void tearDown() {
        testUtils.cleanUp();
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testUser")
    void testShowCartGetEndpoint() throws Exception {
        mockMvc.perform(get("/cart")
                        .with(user(userDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shoppingCartDTO"))
                .andExpect(view().name("shopping-cart"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testAddItemPostEndpoint() throws Exception {
        mockMvc.perform(post("/cart/add")
                        .flashAttr("cartItemDTO", cartItemDTO)
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        ShoppingCartDTO cart = shoppingCartService.viewShoppingCart(userDetails);
        assertFalse(cart.getCartItems().isEmpty());
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDeleteItem() throws Exception {
        shoppingCartService.addItemToCart(cartItemDTO, userDetails);

        ShoppingCartDTO cart = shoppingCartService.viewShoppingCart(userDetails);
        Long itemId = cart.getCartItems().get(0).getId();

        assertNotNull(itemId);

        mockMvc.perform(delete("/cart/delete/{id}", itemId)
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        cart = shoppingCartService.viewShoppingCart(userDetails);
        assertTrue(cart.getCartItems().isEmpty());
    }

    private Book createBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setIsbn("123-456-789");
        book.setPublisher("Test Publisher");
        book.setImageUrl("http://example.com/test.jpg");
        bookRepository.save(book);
        return book;
    }
}