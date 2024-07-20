package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.exceptions.UserNotActivatedException;
import bg.softuni.Online.Book.Store.model.entity.BookStoreUserDetails;
import bg.softuni.Online.Book.Store.model.entity.Role;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookStoreDetailsServiceTest {

    private static final String TEST_EMAIL = "test@email.com";
    private static final String NOT_EXISTENT_EMAIL = "noemail@email.com";

    private BookStoreDetailsService toTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        this.toTest = new BookStoreDetailsService(mockUserRepository);
    }

    @Test
    void testLoadUserByUsernameFound() {

        User testUser = testUser();

        when(mockUserRepository.findByEmail(TEST_EMAIL))
                .thenReturn(Optional.of(testUser));

        UserDetails userDetails = toTest.loadUserByUsername(TEST_EMAIL);

        Assertions.assertInstanceOf(BookStoreUserDetails.class, userDetails);

        BookStoreUserDetails bookStoreUserDetails = (BookStoreUserDetails) userDetails;

        Assertions.assertEquals(TEST_EMAIL, userDetails.getUsername());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(testUser.getFirstName(), bookStoreUserDetails.getFirstName());
        Assertions.assertEquals(testUser.getLastName(), bookStoreUserDetails.getLastName());
        Assertions.assertEquals(testUser.getFirstName() + " " + testUser.getLastName(), bookStoreUserDetails.getFullName());
        Assertions.assertEquals(2, userDetails.getAuthorities().size());

        List<String> expected = testUser.getRoles().stream().map(Role::getName).map(r -> "ROLE_" + r).toList();
        List<String> actual = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Assertions.assertEquals(expected, actual);

    }

    @Test
    void testUserNotActive(){

        User testUser = testUser();
        testUser.setActive(false);

        when(mockUserRepository.findByEmail(TEST_EMAIL))
                .thenReturn(Optional.of(testUser));

        Assertions.assertThrows(UserNotActivatedException.class,
                () -> toTest.loadUserByUsername(TEST_EMAIL));
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername(NOT_EXISTENT_EMAIL));
    }


    private static User testUser() {
        return
                new User()
                        .setEmail(TEST_EMAIL)
                        .setPassword("topsecret")
                        .setFirstName("Pesho")
                        .setLastName("Petrov")
                        .setActive(true)
                        .setRoles(List.of(
                                new Role().setName(UserRole.ADMIN),
                                new Role().setName(UserRole.USER)
                        ));
    }


}