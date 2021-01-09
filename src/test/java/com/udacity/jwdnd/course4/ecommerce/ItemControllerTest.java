package com.udacity.jwdnd.course4.ecommerce;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private final String USERNAME = "Alex";
    private final String PASSWORD = "alexPassword";

    @BeforeEach
    public void initialize() {
        createUser(USERNAME, PASSWORD);
    }

    @Test
    public void testListItems() {
        webTestClient.get().uri("/api/item")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class).hasSize(2);
    }

    @Test
    public void testFindItemById() {
        webTestClient.get().uri("/api/item/{id}", "1")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Item.class).value(item -> {
                        Assertions.assertEquals(1L, item.getId());
                        Assertions.assertEquals("Round Widget", item.getName());
                        Assertions.assertEquals(new BigDecimal("2.99"), item.getPrice());
                });
    }

    @Test
    public void testFindItemsByName() {
        webTestClient.get().uri("/api/item/name/{name}", "Square Widget")
            .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Item.class).hasSize(1).value(items -> {
                    Item item = items.get(0);
                    Assertions.assertEquals(2L, item.getId());
                    Assertions.assertEquals("Square Widget", item.getName());
                    Assertions.assertEquals(new BigDecimal("1.99"), item.getPrice());
            });
    }

    @Test
    public void testFindItemsByNameNotFound() {
        webTestClient.get().uri("/api/item/name/{name}", "Unknown Widget")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
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
}
