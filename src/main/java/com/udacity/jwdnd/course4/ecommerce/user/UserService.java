package com.udacity.jwdnd.course4.ecommerce.user;

import com.udacity.jwdnd.course4.ecommerce.cart.Cart;
import com.udacity.jwdnd.course4.ecommerce.cart.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createUser(CreateUserRequest req) throws UsernameCheckException, PasswordCheckException {
        if (existsUserByUsername(req.getUsername())) {
            logger.debug("could not create user='{}' because username is already taken", req.getUsername());
            throw new UsernameCheckException("username already taken");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            logger.debug("could not create user='{}' because of password confirmation mismatch", req.getUsername());
            throw new PasswordCheckException("password confirmation mismatch");
        }
        if (req.getPassword().length() < 8) {
            logger.debug("could not create user='{}' because of insufficient password complexity", req.getUsername());
            throw new PasswordCheckException("password complexity insufficient");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
        user.setCart(cart);
        userRepository.save(user);
        logger.info("created user='{}' with user_id={}", user.getUsername(), user.getId());
        return user;
    }
}
