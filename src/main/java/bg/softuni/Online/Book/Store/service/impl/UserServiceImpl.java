package bg.softuni.Online.Book.Store.service.impl;

import bg.softuni.Online.Book.Store.events.UserRegisterEvent;
import bg.softuni.Online.Book.Store.model.dto.UserRegisterDTO;
import bg.softuni.Online.Book.Store.model.entity.User;
import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.repository.RoleRepository;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void register(UserRegisterDTO userRegisterDTO) {
        encodePassword(userRegisterDTO);
        User user = modelMapper.map(userRegisterDTO, User.class);
        // TODO: send activation email
        user.setActive(true);
        setRole(user);
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

    private void encodePassword(UserRegisterDTO userRegisterDTO) {
        userRegisterDTO.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
    }

    private void setRole(User user) {
        user.getRoles().add(roleRepository.findByName(UserRole.USER));
    }


}
