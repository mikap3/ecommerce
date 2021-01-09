package com.udacity.jwdnd.course4.ecommerce;

import com.udacity.jwdnd.course4.ecommerce.cart.Cart;
import com.udacity.jwdnd.course4.ecommerce.cart.ModifyCartRequest;
import com.udacity.jwdnd.course4.ecommerce.order.Order;
import com.udacity.jwdnd.course4.ecommerce.user.CreateUserRequest;
import com.udacity.jwdnd.course4.ecommerce.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private final String USERNAME = "Alex";
    private final String PASSWORD = "alexPassword";

    @BeforeEach
    public void initialize() {
        createUser(USERNAME, PASSWORD);
    }

    @Test
    public void testSubmitOrder() {
        addToCart(1L, 2);
        addToCart(2L, 1);

        webTestClient.post().uri("/api/order/submit")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Order.class).value(order -> {
                        Assertions.assertNotNull(order.getId());
                        Assertions.assertEquals(3, order.getItems().size());
                        Assertions.assertEquals(new BigDecimal("7.97"), order.getTotal());
                });
    }

    @Test
    public void testListOrders() {
        addToCart(1L, 2);
        submitOrder();
        addToCart(2L, 1);
        submitOrder();

        webTestClient.get().uri("/api/order/history")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Order.class).hasSize(2);
    }

    private User createUser(String username, String password) {
        return webTestClient.post().uri("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest(username, password, password))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(User.class).value(user -> {
                        Assertions.assertNotNull(user.getId());
                        Assertions.assertEquals(username, user.getUsername());
                        Assertions.assertNull(user.getPassword());
                })
                .returnResult().getResponseBody();
    }

    private Cart addToCart(Long itemId, Integer quantity) {
        return webTestClient.post().uri("/api/cart/addToCart")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ModifyCartRequest(itemId, quantity))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cart.class)
                .returnResult().getResponseBody();
    }

    private Order submitOrder() {
        return webTestClient.post().uri("/api/order/submit")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Order.class)
                .returnResult().getResponseBody();
    }
}
