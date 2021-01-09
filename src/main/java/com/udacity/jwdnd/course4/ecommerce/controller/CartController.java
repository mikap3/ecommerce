package com.udacity.jwdnd.course4.ecommerce.controller;

import com.udacity.jwdnd.course4.ecommerce.dto.ModifyCartRequest;
import com.udacity.jwdnd.course4.ecommerce.entity.Cart;
import com.udacity.jwdnd.course4.ecommerce.entity.User;
import com.udacity.jwdnd.course4.ecommerce.service.CartService;
import com.udacity.jwdnd.course4.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
