package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.events.UserRegisterEvent;
import bg.softuni.Online.Book.Store.exceptions.FieldError;
import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.exceptions.ValidationException;
import bg.softuni.Online.Book.Store.model.dto.user.ChangePasswordDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserProfileDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserRegisterDTO;
import bg.softuni.Online.Book.Store.model.entity.Role;
import bg.softuni.Online.Book.Store.model.entity.ShoppingCart;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.RoleRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.ShoppingCartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UserRegisterEvent> eventCaptor;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private BookStoreDetailsService bookStoreDetailsService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    void setUp() {

    }

    @Test
    void testRegistration() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO()
                .setFirstName("Pesho")
                .setLastName("Petrov")
                .setEmail("test@test.com")
                .setPassword("password")
                .setAge(22)
                .setUsername("username")
                .setConfirmPassword("password");

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setFirstName(userRegisterDTO.getFirstName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword());
        user.setAge(userRegisterDTO.getAge());

        when(modelMapper.map(any(UserRegisterDTO.class), eq(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->
                invocation.getArgument(0));

        userService.register(userRegisterDTO);
        verify(userRepository).save(userCaptor.capture());
        verify(emailService).sendActivationEmail(eq(user));
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        User savedUser = userCaptor.getValue();

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(userRegisterDTO.getFirstName(), savedUser.getFirstName());
        Assertions.assertEquals(userRegisterDTO.getLastName(), savedUser.getLastName());
        Assertions.assertEquals(userRegisterDTO.getEmail(), savedUser.getEmail());
        Assertions.assertEquals(userRegisterDTO.getPassword(), savedUser.getPassword());

        UserRegisterEvent event = eventCaptor.getValue();
        assertNotNull(event);
        assertEquals(user, event.getUser());
    }

    @Test
    void testUpdateLastLoginDate() {
        User user = new User();
        LocalDate now = LocalDate.now();
        user.setLastLoginDate(null);

        userService.updateUserLastLogin(user);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(now, savedUser.getLastLoginDate());
        Assertions.assertNotEquals(now.plusDays(1), savedUser.getLastLoginDate());
    }

    @Test
    void testFindUserByEmail() {
        User user = createUser();
        String email = user.getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        User userByEmail = userService.findUserByEmail(email);
        assertNotNull(userByEmail);
        assertEquals(email, userByEmail.getEmail());
    }

    @Test
    void testFindUserByEmailNotFoundException() {
        String email = "wrong@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.findUserByEmail(email));
    }

    @Test
    void testIsUserExistsTrue() {
        User user = createUser();
        String email = user.getEmail();
        String username = user.getUsername();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertTrue(userService.isUserExist(username, email));
    }

    @Test
    void testIsUserExistsFalse() {
        User user = createUser();
        String email = user.getEmail();
        String username = user.getUsername();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertFalse(userService.isUserExist(username, email));
    }

    @Test
    void testCreateUserIfNotExistsShouldCreateUserAndSetFields() {
        String email = "test@test.com";
        String name = "Test User";
        String encodedPassword = "encodedPassword";
        long userCount = 1;
        Role role = new Role();
        role.setName(UserRole.USER);

        ShoppingCart shoppingCart = new ShoppingCart();

        when(userRepository.count()).thenReturn(userCount);
        when(roleRepository.findByName(UserRole.USER)).thenReturn(role);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(shoppingCartService.getShoppingCartByUser(any(User.class))).thenReturn(shoppingCart);

        userService.createUserIfNotExist(email, name);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser);
        assertEquals(email, savedUser.getEmail());
        assertEquals(name, savedUser.getUsername());
        assertEquals("Test", savedUser.getFirstName());
        assertEquals("User", savedUser.getLastName());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertNotNull(savedUser.getLastLoginDate());
        assertTrue(savedUser.isActive());
        assertEquals(List.of(role), savedUser.getRoles());
    }

    @Test
    void testLoginOauthSuccess() {
        String email = "test@test.com";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication expectedAuth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());

        when(bookStoreDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        Authentication actualAuth = userService.login(email);

        assertEquals(expectedAuth, actualAuth);
        assertEquals(expectedAuth, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testLoginOauthFailure() {
        String email = "notexisting@test.com";
        assertThrows(NullPointerException.class, () -> userService.login(email));
    }

    @Test
    void testLoginOauthShouldSetAuthentication() {
        String email = "test@test.com";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication expectedAuth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
        when(bookStoreDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        userService.login(email);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        assertNotNull(authentication);
        assertEquals(expectedAuth.getPrincipal(), authentication.getPrincipal());
        assertEquals(expectedAuth.getCredentials(), authentication.getCredentials());
        assertEquals(expectedAuth.getAuthorities(), authentication.getAuthorities());
    }

    @Test
    void testGetUserDetailsWithValidUser() {
        Long id = 1L;
        User user = createUser();
        user.setId(id);

        UserProfileDTO expected = createUserProfileDTO();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserProfileDTO.class)).thenReturn(expected);

        UserProfileDTO actual = userService.getUserDetails(id);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository).findById(id);
        verify(modelMapper).map(user, UserProfileDTO.class);
    }

    @Test
    void testGetUserDetailsWithInvalidUser() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> userService.getUserDetails(id));
    }

    @Test
    void testValidateUserProfileReturnUser() {
        Long id = 1L;
        UserProfileDTO userProfileDTO = createUserProfileDTO();
        User user = createUser();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.validateUserProfile(userProfileDTO));
        verify(userRepository).findById(id);
    }

    @Test
    void testValidateUserProfileShouldThrowException() {
        Long id = 1L;
        UserProfileDTO userProfileDTO = createUserProfileDTO();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.validateUserProfile(userProfileDTO));
        verify(userRepository).findById(id);
    }

    @Test
    void testValidateUserProfileShouldAddErrorWhenUsernameExists() {
        Long userId = 1L;
        UserProfileDTO userProfileDTO = new UserProfileDTO()
                .setId(userId)
                .setUsername("newUsername")
                .setEmail("validEmail@test.com");

        User user = new User()
                .setUsername("currentUsername")
                .setEmail("validEmail@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("newUsername")).thenReturn(Optional.of(new User()));


        ValidationException thrownException = assertThrows(
                ValidationException.class,
                () -> userService.validateUserProfile(userProfileDTO)
        );

        List<FieldError> errors = thrownException.getFieldErrors();
        assertEquals(1, errors.size());
    }

    @Test
    void testValidateUserProfileShouldAddErrorWhenEmailExists() {
        Long userId = 1L;
        UserProfileDTO userProfileDTO = new UserProfileDTO()
                .setId(userId)
                .setUsername("validUsername")
                .setEmail("newEmail@example.com");

        User user = new User()
                .setUsername("validUsername")
                .setEmail("currentEmail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("newEmail@example.com")).thenReturn(Optional.of(new User()));

        ValidationException thrownException = assertThrows(
                ValidationException.class,
                () -> userService.validateUserProfile(userProfileDTO)
        );

        List<FieldError> errors = thrownException.getFieldErrors();
        assertEquals(1, errors.size());
    }

    @Test
    void testValidateUserProfileNotAddingErrorWhenUsernameIsTheSame() {
        Long userId = 1L;
        UserProfileDTO userProfileDTO = new UserProfileDTO()
                .setId(userId)
                .setUsername("currentUsername")
                .setEmail("newEmail@example.com");

        User user = new User()
                .setUsername("currentUsername")
                .setEmail("currentEmail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("newEmail@example.com")).thenReturn(Optional.empty());

        userService.validateUserProfile(userProfileDTO);
    }

    @Test
    void testValidateUserProfileNotAddingErrorWhenEmailIsTheSame() {
        Long userId = 1L;
        UserProfileDTO userProfileDTO = new UserProfileDTO()
                .setId(userId)
                .setUsername("newUsername")
                .setEmail("currentEmail@example.com");

        User user = new User()
                .setUsername("currentUsername")
                .setEmail("currentEmail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("newUsername")).thenReturn(Optional.empty());

        userService.validateUserProfile(userProfileDTO);
    }

    @Test
    void testEditProfileShouldThrowIfUserDoesNotExist() {
        Long id = 1L;
        UserProfileDTO userProfileDTO = createUserProfileDTO();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> userService.editProfile(userProfileDTO));
        verify(userRepository).findById(id);
    }

    @Test
    void testEditProfileMapUserToDTO() {
        Long userId = 1L;
        UserProfileDTO userProfileDTO = createUserProfileDTO();

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("CurrentFirstName");
        existingUser.setLastName("CurrentLastName");
        existingUser.setUsername("currentUsername");
        existingUser.setEmail("currentEmail@example.com");
        existingUser.setAge(25);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        doAnswer(invocation -> {
            UserProfileDTO source = invocation.getArgument(0);
            User destination = invocation.getArgument(1);
            destination.setId(source.getId());
            destination.setFirstName(source.getFirstName());
            destination.setLastName(source.getLastName());
            destination.setUsername(source.getUsername());
            destination.setEmail(source.getEmail());
            destination.setAge(source.getAge());
            return null;
        }).when(modelMapper).map(any(UserProfileDTO.class), any(User.class));

        userService.editProfile(userProfileDTO);

        verify(userRepository).findById(userId);
        verify(modelMapper).map(eq(userProfileDTO), eq(existingUser));
        verify(userRepository).save(existingUser);

        assertEquals("Pesho", existingUser.getFirstName());
        assertEquals("Petrov", existingUser.getLastName());
        assertEquals("username", existingUser.getUsername());
        assertEquals("test@test.com", existingUser.getEmail());
        assertEquals(22, existingUser.getAge());
    }

    @Test
    void testChangePasswordShouldThrowIfUserDoesNotExist() {
        Long id = 1L;
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.changePassword(id, changePasswordDTO));
        verify(userRepository).findById(id);
    }

    @Test
    void testChangePasswordShouldSetEncodedPasswordAndSaveUser() {
        Long id = 1L;
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setPassword("newPassword");
        User user = new User();
        user.setId(id);
        user.setPassword("oldPassword");
        String encodedPassword = "encodedNewPassword";

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(changePasswordDTO.getPassword())).thenReturn(encodedPassword);
        userService.changePassword(id, changePasswordDTO);

        verify(userRepository).findById(id);
        verify(passwordEncoder).encode(changePasswordDTO.getPassword());
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(encodedPassword, savedUser.getPassword());
    }

    @Test
    void testValidateOldPasswordShouldThrowIfUserDoesNotExist() {
        Long id = 1L;
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.validateOldPassword(id, changePasswordDTO));
        verify(userRepository).findById(id);
    }

    @Test
    void testValidateOldPasswordShouldAddErrorWhenOldPasswordNotMatchUserPassword() {
        Long id = 1L;
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("wrongOldPassword");
        User user = new User();
        user.setId(id);
        user.setPassword("oldPassword");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())).thenReturn(false);

        ValidationException validationException =
                assertThrows(ValidationException.class,
                        () -> userService.validateOldPassword(id, changePasswordDTO));
        verify(userRepository).findById(id);

        assertEquals(1, validationException.getFieldErrors().size());
    }

    @Test
    void testValidateOldPasswordShouldNotAddErrorWhenOldPasswordMatchUserPassword() {
        Long id = 1L;
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("oldPassword");
        User user = new User();
        user.setId(id);
        user.setPassword("oldPassword");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())).thenReturn(true);

        userService.validateOldPassword(id, changePasswordDTO);
        verify(userRepository).findById(id);
    }

    @Test
    void testActivateAccountReturnFalseWhenTokenAndUserNotMatch() {
        String token = "token";

        when(userRepository.findByActivationToken(token)).thenReturn(null);

        assertFalse(userService.activateAccount(token));
    }

    @Test
    void testActivateAccountReturnFalseWhenTokenIsExpired() {
        String token = "token";
        User user = new User();
        user.setActivationToken(token);
        user.setTokenExpiryDate(LocalDate.now().minusDays(1));

        when(userRepository.findByActivationToken(token)).thenReturn(user);

        assertFalse(userService.activateAccount(token));
    }

    @Test
    void testActivateAccountReturnFalseWhenTokenIsNull() {
        String token = "token";
        User user = new User();
        user.setActivationToken(token);
        user.setTokenExpiryDate(null);

        when(userRepository.findByActivationToken(token)).thenReturn(user);

        assertFalse(userService.activateAccount(token));
    }

    @Test
    void testActivateAccountShouldActivateUserWhenTokenIsValid() {
        String token = "token";
        User user = new User();
        user.setActivationToken(token);
        user.setTokenExpiryDate(LocalDate.now().plusDays(1));
        user.setActive(false);

        when(userRepository.findByActivationToken(token)).thenReturn(user);

        assertTrue(userService.activateAccount(token));

        verify(userRepository).findByActivationToken(token);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertTrue(savedUser.isActive());
        assertNull(savedUser.getTokenExpiryDate());
        assertNull(savedUser.getActivationToken());
    }

    @Test
    void testRequestActivationEmailShouldReturnFalseWhenUserNotExists() {
        String email = "notExistEmail@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertFalse(userService.requestActivationEmail(email));
    }

    @Test
    void testRequestActivationEmailShouldReturnTrueWhenEmailIsValid() {
        String email = "validEmail@example.com";
        User user = new User();
        user.setEmail(email);
        user.setTokenExpiryDate(null);
        user.setActivationToken(null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertTrue(userService.requestActivationEmail(email));
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser.getTokenExpiryDate());
        assertNotNull(savedUser.getActivationToken());
    }

    private User createUser() {
        return new User()
                .setFirstName("Pesho")
                .setLastName("Petrov")
                .setEmail("test@test.com")
                .setPassword("password")
                .setAge(22)
                .setUsername("username")
                .setRoles(List.of(
                        new Role().setName(UserRole.ADMIN),
                        new Role().setName(UserRole.USER)
                ))
                .setActivationToken(UUID.randomUUID().toString())
                .setActive(true)
                .setLastLoginDate(null);
    }

    private UserProfileDTO createUserProfileDTO() {
        return new UserProfileDTO()
                .setFirstName("Pesho")
                .setLastName("Petrov")
                .setEmail("test@test.com")
                .setAge(22)
                .setUsername("username")
                .setId(1L);
    }

}