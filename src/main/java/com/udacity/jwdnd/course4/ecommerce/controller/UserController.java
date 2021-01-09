package com.udacity.jwdnd.course4.ecommerce.controller;

import com.udacity.jwdnd.course4.ecommerce.dto.CreateUserRequest;
import com.udacity.jwdnd.course4.ecommerce.entity.User;
import com.udacity.jwdnd.course4.ecommerce.exception.PasswordCheckException;
import com.udacity.jwdnd.course4.ecommerce.exception.UsernameCheckException;
import com.udacity.jwdnd.course4.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        try {
            User user = userService.createUser(createUserRequest);
            return ResponseEntity.ok(user);
        }
        catch (UsernameCheckException | PasswordCheckException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
