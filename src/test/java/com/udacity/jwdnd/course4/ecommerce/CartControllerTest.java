package com.udacity.jwdnd.course4.ecommerce;

import com.udacity.jwdnd.course4.ecommerce.cart.Cart;
import com.udacity.jwdnd.course4.ecommerce.cart.ModifyCartRequest;
import com.udacity.jwdnd.course4.ecommerce.item.Item;
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
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private final String USERNAME = "Alex";
    private final String PASSWORD = "alexPassword";

    @BeforeEach
    public void initialize() {
        createUser(USERNAME, PASSWORD);
    }

    @Test
    public void testAddToCart() {
        webTestClient.post().uri("/api/cart/addToCart")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ModifyCartRequest(1L, 3))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cart.class).value(cart -> {
                        Assertions.assertNotNull(cart.getId());
                        List<Item> items = cart.getItems();
                        Assertions.assertEquals(3, items.size());
                        Assertions.assertEquals(1L, items.get(0).getId());
                        Assertions.assertEquals(1L, items.get(1).getId());
                        Assertions.assertEquals(1L, items.get(2).getId());
                        Assertions.assertEquals(new BigDecimal("8.97"), cart.getTotal());
                });
    }

    @Test
    public void testRemoveFromCart() {
        addToCart(1L, 3);

        webTestClient.post().uri("/api/cart/removeFromCart")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ModifyCartRequest(1L, 1))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cart.class).value(cart -> {
                        Assertions.assertNotNull(cart.getId());
                        List<Item> items = cart.getItems();
                        Assertions.assertEquals(2, items.size());
                        Assertions.assertEquals(1L, items.get(0).getId());
                        Assertions.assertEquals(1L, items.get(1).getId());
                        Assertions.assertEquals(new BigDecimal("5.98"), cart.getTotal());
                });
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
}
