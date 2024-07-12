package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.events.UserRegisterEvent;
import bg.softuni.Online.Book.Store.exceptions.FieldError;
import bg.softuni.Online.Book.Store.exceptions.ValidationException;
import bg.softuni.Online.Book.Store.exceptions.ObjectNotFoundException;
import bg.softuni.Online.Book.Store.model.dto.user.UserProfileDTO;
import bg.softuni.Online.Book.Store.model.dto.user.UserRegisterDTO;
import bg.softuni.Online.Book.Store.model.entity.ShoppingCart;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.RoleRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.ShoppingCartService;
import bg.softuni.Online.Book.Store.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static bg.softuni.Online.Book.Store.constants.Exceptions.USER_NOT_FOUND;
import static bg.softuni.Online.Book.Store.constants.Exceptions.USER_WITH_EMAIL_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ShoppingCartService shoppingCartService;
    private final BookStoreDetailsService bookStoreDetailsService;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           ApplicationEventPublisher eventPublisher,
                           ShoppingCartService shoppingCartService,
                           BookStoreDetailsService bookStoreDetailsService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.eventPublisher = eventPublisher;
        this.shoppingCartService = shoppingCartService;
        this.bookStoreDetailsService = bookStoreDetailsService;
    }

    @Override
    @Transactional
    public void register(UserRegisterDTO userRegisterDTO) {
        encodePassword(userRegisterDTO);
        User user = modelMapper.map(userRegisterDTO, User.class);
        // TODO: send activation email
        user.setActive(true);
        setRoles(user);
        saveUser(user);
        eventPublisher.publishEvent(new UserRegisterEvent(user));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUserLastLogin(User user) {
        user.setLastLoginDate(LocalDate.now());
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(USER_WITH_EMAIL_NOT_FOUND));
    }

    @Override
    public boolean isUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public void createUserIfNotExist(String email, String name) {
        User user = new User();
        setRoles(user);
        setUserFields(email, name, user);
        saveUser(user);
        setUserShoppingCart(user);
    }

    @Override
    public Authentication login(String email) {
        UserDetails userDetails = bookStoreDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public UserProfileDTO getUserDetails(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format(USER_NOT_FOUND, id)));
        return modelMapper.map(user, UserProfileDTO.class);
    }

    @Override
    public void validateUserProfile(UserProfileDTO userProfileDTO) {
        Long id = userProfileDTO.getId();
        User user = userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format(USER_NOT_FOUND, id)));

        String editedUsername = userProfileDTO.getUsername();
        String currentUsername = user.getUsername();

        String editedEmail = userProfileDTO.getEmail();
        String currentEmail = user.getEmail();

        List<FieldError> errors = new ArrayList<>();

        if (!currentUsername.equals(editedUsername) && userRepository.findByUsername(editedUsername).isPresent()) {
            errors.add(new FieldError("username", "Username already exists"));
        }

        if (!currentEmail.equals(editedEmail) && userRepository.findByEmail(editedEmail).isPresent()) {
            errors.add(new FieldError("email", "Email already exists"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void editProfile(UserProfileDTO userProfileDTO) {
        Long id = userProfileDTO.getId();
        User user = userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format(USER_NOT_FOUND, id)));
        modelMapper.map(userProfileDTO, user);
        saveUser(user);
    }

    private void setUserShoppingCart(User user) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUser(user);
        user.setShoppingCart(shoppingCart);
    }

    private void setUserFields(String email, String name, User user) {
        user.setEmail(email)
                .setUsername(name)
                .setFirstName(name.split(" ")[0])
                .setLastName(name.split(" ")[1])
                .setPassword(passwordEncoder.encode(UUID.randomUUID().toString()))
                .setActive(true)
                .setLastLoginDate(LocalDate.now())
                .setAge(new Random().nextInt(100));
    }

    private void encodePassword(UserRegisterDTO userRegisterDTO) {
        userRegisterDTO.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
    }

    private void setRoles(User user) {
        if (this.userRepository.count() == 0) {
            user.setRoles(roleRepository.findAll());
        } else {
            user.setRoles(List.of(roleRepository.findByName(UserRole.USER)));
        }
    }
}
