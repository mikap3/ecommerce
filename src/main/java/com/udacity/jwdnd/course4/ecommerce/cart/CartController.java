package com.udacity.jwdnd.course4.ecommerce.cart;

import com.udacity.jwdnd.course4.ecommerce.user.User;
import com.udacity.jwdnd.course4.ecommerce.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@Valid @RequestBody ModifyCartRequest modifyCartRequest, Authentication auth) {
        User user = userService.findUserByUsername(auth.getName());
        Cart cart = cartService.addToCart(user, modifyCartRequest);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@Valid @RequestBody ModifyCartRequest modifyCartRequest, Authentication auth) {
        User user = userService.findUserByUsername(auth.getName());
        Cart cart = cartService.removeFromCart(user, modifyCartRequest);
        return ResponseEntity.ok(cart);
    }
}
