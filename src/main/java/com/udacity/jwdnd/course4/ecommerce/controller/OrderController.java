package com.udacity.jwdnd.course4.ecommerce.controller;

import com.udacity.jwdnd.course4.ecommerce.entity.User;
import com.udacity.jwdnd.course4.ecommerce.entity.UserOrder;
import com.udacity.jwdnd.course4.ecommerce.service.OrderService;
import com.udacity.jwdnd.course4.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/submit")
    public ResponseEntity<UserOrder> submitOrder(Authentication auth) {
        User user = userService.findUserByUsername(auth.getName());
        UserOrder order = orderService.submitOrderOfUser(user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history")
    public ResponseEntity<List<UserOrder>> listOrders(Authentication auth) {
        User user = userService.findUserByUsername(auth.getName());
        List<UserOrder> orders = orderService.findOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }
}
