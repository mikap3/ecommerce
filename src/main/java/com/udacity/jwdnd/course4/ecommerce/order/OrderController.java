package com.udacity.jwdnd.course4.ecommerce.order;

import com.udacity.jwdnd.course4.ecommerce.user.User;
import com.udacity.jwdnd.course4.ecommerce.user.UserService;
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
    public ResponseEntity<Order> submitOrder(Authentication auth) {
        User user = userService.findUserByUsername(auth.getName());
        Order order = orderService.submitOrderOfUser(user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Order>> listOrders(Authentication auth) {
        User user = userService.findUserByUsername(auth.getName());
        List<Order> orders = orderService.findOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }
}
