package com.udacity.jwdnd.course4.ecommerce;

import com.udacity.jwdnd.course4.ecommerce.dto.CreateUserRequest;
import com.udacity.jwdnd.course4.ecommerce.dto.ModifyCartRequest;
import com.udacity.jwdnd.course4.ecommerce.entity.Cart;
import com.udacity.jwdnd.course4.ecommerce.entity.Item;
import com.udacity.jwdnd.course4.ecommerce.entity.User;
import com.udacity.jwdnd.course4.ecommerce.entity.UserOrder;
import com.udacity.jwdnd.course4.ecommerce.controller.CartController;
import com.udacity.jwdnd.course4.ecommerce.controller.ItemController;
import com.udacity.jwdnd.course4.ecommerce.controller.OrderController;
import com.udacity.jwdnd.course4.ecommerce.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
public class EcommerceApplicationTests {

    @Autowired
    private UserController userController;
    @Autowired
    private ItemController itemController;
    @Autowired
    private CartController cartController;
    @Autowired
    private OrderController orderController;

    @Test
    public void testCreateUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "alexPassword", "alexPassword");
        ResponseEntity<User> createUserResponse = userController.createUser(createUserRequest);
        Assertions.assertEquals(HttpStatus.OK, createUserResponse.getStatusCode());
        User user = createUserResponse.getBody();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals("Alex", user.getUsername());
    }

    @Test
    public void testCreateUserPasswordConfirmationMismatch() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "alexPassword", "wrongPassword");
        ResponseEntity<User> createUserResponse = userController.createUser(createUserRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, createUserResponse.getStatusCode());
        Assertions.assertNull(createUserResponse.getBody());
    }

    @Test
    public void testCreateUserPasswordComplexityInsufficient() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "1234", "1234");
        ResponseEntity<User> createUserResponse = userController.createUser(createUserRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, createUserResponse.getStatusCode());
        Assertions.assertNull(createUserResponse.getBody());
    }

    @Test
    public void testCreateUserUsernameAlreadyTaken() {
        CreateUserRequest createUserRequest1 = new CreateUserRequest("Alex", "alexPassword", "alexPassword");
        userController.createUser(createUserRequest1);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Alex", "anotherPassword", "anotherPassword");
        ResponseEntity<User> createUserResponse2 = userController.createUser(createUserRequest2);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, createUserResponse2.getStatusCode());
        Assertions.assertNull(createUserResponse2.getBody());
    }

    @Test
    public void testFindUserById() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "alexPassword", "alexPassword");
        Long userId = userController.createUser(createUserRequest).getBody().getId();

        ResponseEntity<User> findUserByIdResponse = userController.findUserById(userId);
        Assertions.assertEquals(HttpStatus.OK, findUserByIdResponse.getStatusCode());
        User user = findUserByIdResponse.getBody();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getId());
        Assertions.assertEquals("Alex", user.getUsername());
    }

    @Test
    public void testFindUserByUsername() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "alexPassword", "alexPassword");
        Long userId = userController.createUser(createUserRequest).getBody().getId();

        ResponseEntity<User> findUserByUsernameResponse = userController.findUserByUsername("Alex");
        Assertions.assertEquals(HttpStatus.OK, findUserByUsernameResponse.getStatusCode());
        User user = findUserByUsernameResponse.getBody();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getId());
        Assertions.assertEquals("Alex", user.getUsername());
    }

    @Test
    public void testAddToCart() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "alexPassword", "alexPassword");
        userController.createUser(createUserRequest);
        Authentication authentication = new UsernamePasswordAuthenticationToken(createUserRequest.getUsername(), null, new ArrayList<>());

        ModifyCartRequest addToCartRequest = new ModifyCartRequest();
        addToCartRequest.setItemId(1L);
        addToCartRequest.setQuantity(3);
        ResponseEntity<Cart> addToCartResponse = cartController.addToCart(addToCartRequest, authentication);
        Assertions.assertEquals(HttpStatus.OK, addToCartResponse.getStatusCode());
        Cart cart = addToCartResponse.getBody();
        Assertions.assertNotNull(cart);
        Assertions.assertNotNull(cart.getId());
        List<Item> items = cart.getItems();
        Assertions.assertEquals(3, items.size());
        Assertions.assertEquals(1L, items.get(0).getId());
        Assertions.assertEquals(1L, items.get(1).getId());
        Assertions.assertEquals(1L, items.get(2).getId());
        Assertions.assertEquals(new BigDecimal("8.97"), cart.getTotal());
    }

    @Test
    public void testRemoveFromCart() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "alexPassword", "alexPassword");
        userController.createUser(createUserRequest);
        Authentication authentication = new UsernamePasswordAuthenticationToken(createUserRequest.getUsername(), null, new ArrayList<>());

        ModifyCartRequest addToCartRequest = new ModifyCartRequest(1L, 3);
        cartController.addToCart(addToCartRequest, authentication);

        ModifyCartRequest removeFromCartRequest = new ModifyCartRequest(1L, 1);
        ResponseEntity<Cart> removeFromCartResponse = cartController.removeFromCart(removeFromCartRequest, authentication);
        Assertions.assertEquals(HttpStatus.OK, removeFromCartResponse.getStatusCode());
        Cart cart = removeFromCartResponse.getBody();
        Assertions.assertNotNull(cart);
        Assertions.assertNotNull(cart.getId());
        List<Item> items = cart.getItems();
        Assertions.assertEquals(2, items.size());
        Assertions.assertEquals(1L, items.get(0).getId());
        Assertions.assertEquals(1L, items.get(1).getId());
        Assertions.assertEquals(new BigDecimal("5.98"), cart.getTotal());
    }

    @Test
    public void testSubmitOrder() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "alexPassword", "alexPassword");
        userController.createUser(createUserRequest);
        Authentication authentication = new UsernamePasswordAuthenticationToken(createUserRequest.getUsername(), null, new ArrayList<>());

        ModifyCartRequest addToCartRequest1 = new ModifyCartRequest(1L, 2);
        cartController.addToCart(addToCartRequest1, authentication);
        ModifyCartRequest addToCartRequest2 = new ModifyCartRequest(2L, 1);
        cartController.addToCart(addToCartRequest2, authentication);

        ResponseEntity<UserOrder> submitOrderResponse = orderController.submitOrder(authentication);
        Assertions.assertEquals(HttpStatus.OK, submitOrderResponse.getStatusCode());
        UserOrder order = submitOrderResponse.getBody();
        Assertions.assertNotNull(order);
        Assertions.assertNotNull(order.getId());
        List<Item> items = order.getItems();
        Assertions.assertEquals(3, items.size());
        Assertions.assertEquals(new BigDecimal("7.97"), order.getTotal());
    }

    @Test
    public void testListOrders() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Alex", "alexPassword", "alexPassword");
        userController.createUser(createUserRequest);
        Authentication authentication = new UsernamePasswordAuthenticationToken(createUserRequest.getUsername(), null, new ArrayList<>());

        ModifyCartRequest addToCartRequest1 = new ModifyCartRequest(1L, 2);
        cartController.addToCart(addToCartRequest1, authentication);
        orderController.submitOrder(authentication);
        ModifyCartRequest addToCartRequest2 = new ModifyCartRequest(2L, 1);
        cartController.addToCart(addToCartRequest2, authentication);
        orderController.submitOrder(authentication);

        ResponseEntity<List<UserOrder>> listOrdersResponse = orderController.listOrders(authentication);
        Assertions.assertEquals(HttpStatus.OK, listOrdersResponse.getStatusCode());
        List<UserOrder> orders = listOrdersResponse.getBody();
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(2, orders.size());
    }

    @Test
    public void testListItems() {
        ResponseEntity<List<Item>> listItemsResponse = itemController.listItems();
        Assertions.assertEquals(HttpStatus.OK, listItemsResponse.getStatusCode());
        List<Item> items = listItemsResponse.getBody();
        Assertions.assertNotNull(items);
        Assertions.assertEquals(2, items.size());
    }

    @Test
    public void testFindItemById() {
        ResponseEntity<Item> findItemByIdResponse = itemController.findItemById(1L);
        Assertions.assertEquals(HttpStatus.OK, findItemByIdResponse.getStatusCode());
        Item item = findItemByIdResponse.getBody();
        Assertions.assertNotNull(item);
        Assertions.assertEquals(1L, item.getId());
        Assertions.assertEquals("Round Widget", item.getName());
        Assertions.assertEquals(new BigDecimal("2.99"), item.getPrice());
    }

    @Test
    public void testFindItemsByName() {
        ResponseEntity<List<Item>> findItemsByNameResponse = itemController.findItemsByName("Square Widget");
        Assertions.assertEquals(HttpStatus.OK, findItemsByNameResponse.getStatusCode());
        List<Item> items = findItemsByNameResponse.getBody();
        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());
        Item item = items.get(0);
        Assertions.assertEquals(2L, item.getId());
        Assertions.assertEquals("Square Widget", item.getName());
        Assertions.assertEquals(new BigDecimal("1.99"), item.getPrice());
    }

    @Test
    public void testFindItemsByNameNotFound() {
        ResponseEntity<List<Item>> findItemsByNameResponse = itemController.findItemsByName("Unknown Widget");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, findItemsByNameResponse.getStatusCode());
    }
}
