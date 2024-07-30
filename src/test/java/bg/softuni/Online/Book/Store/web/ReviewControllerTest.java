package bg.softuni.Online.Book.Store.web;

import bg.softuni.Online.Book.Store.model.dto.review.ReviewDTO;
import bg.softuni.Online.Book.Store.model.entity.*;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.BookRepository;
import bg.softuni.Online.Book.Store.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    private static final String USER_EMAIL = "test@test.com";
    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String OTHER_USER_EMAIL = "other@other.com";

    private static final String USER_USERNAME = "user";
    private static final String ADMIN_USERNAME = "admin";
    private static final String OTHER_USER_USERNAME = "otherUSer";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private ReviewDTO reviewDTO;

    private Review review;

    private Book book;

    private User user;

    private User otherUser;

    private User admin;

    private BookStoreUserDetails userDetails;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        testUtils.cleanUp();
        bookRepository.deleteAll();

        Role roleUser = testUtils.createRoleUser(UserRole.USER);
        Role roleAdmin = testUtils.createRoleUser(UserRole.ADMIN);
        user = testUtils.createUser(roleUser, USER_USERNAME, USER_EMAIL);
        admin = testUtils.createUser(roleAdmin, ADMIN_USERNAME, ADMIN_EMAIL);
        otherUser = testUtils.createUser(roleUser, OTHER_USER_USERNAME, OTHER_USER_EMAIL);
        userDetails = testUtils.createUserDetails(user);


        book = new Book()
                .setTitle("test title")
                .setAuthor("test author")
                .setPrice(BigDecimal.valueOf(100.0))
                .setIsbn("ISBN")
                .setPublisher("Publisher")
                .setImageUrl("http://example.com/test.jpg");
        bookRepository.save(book);

        reviewDTO = new ReviewDTO()
                .setId(1L)
                .setRating(5)
                .setComment("test comment")
                .setCreated(LocalDate.now())
                .setBookId(book.getId());
        review = new Review()
                .setRating(5)
                .setComment("test comment")
                .setCreated(LocalDate.now())
                .setBook(book)
                .setUser(user);

        reviewRepository.save(review);
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll();
        testUtils.cleanUp();
        bookRepository.deleteAll();
    }

    @Test
    void testAddReviewPostEndpoint() throws Exception {
        mockMvc.perform(post("/review/add")
                        .with(csrf())
                        .with(user(userDetails))
                        .param("bookId", reviewDTO.getBookId().toString())
                        .param("rating", String.valueOf(reviewDTO.getRating()))
                        .param("comment", reviewDTO.getComment())
                        .param("created", reviewDTO.getCreated().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/details/" + book.getId()));
    }

    @Test
    @WithMockUser(username = "user")
    void testUserCanDeleteOwnReview() throws Exception {
        mockMvc.perform(delete("/review/delete/{id}", review.getId())
                        .param("bookId", book.getId().toString())
                        .with(csrf())
                        .with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/details/" + book.getId()));

        assertTrue(reviewRepository.findById(review.getId()).isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCanDeleteAnyReview() throws Exception {
        mockMvc.perform(delete("/review/delete/{id}", review.getId())
                        .param("bookId", book.getId().toString())
                        .with(csrf())
                        .with(user(testUtils.createUserDetails(admin))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/details/" + book.getId()));
        assertTrue(reviewRepository.findById(review.getId()).isEmpty());
    }

    @Test
    @WithMockUser(username = "otherUser")
    void testUserCanNotDeleteReviewTheyDidNotCreate() throws Exception {
        mockMvc.perform(delete("/review/delete/{id}", review.getId())
                        .param("bookId", book.getId().toString())
                        .with(csrf())
                        .with(user(testUtils.createUserDetails(otherUser))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books/details/" + book.getId() + "?error=notAuthorized"));

        assertTrue(reviewRepository.findById(review.getId()).isPresent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteNonExistentReview() throws Exception {
        mockMvc.perform(delete("/review/delete/{id}", 999L)
                        .param("bookId", book.getId().toString())
                        .with(csrf())
                        .with(user(testUtils.createUserDetails(admin))))

                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books/details/" + book.getId() + "?error=reviewNotFound"));
    }

}