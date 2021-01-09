package com.udacity.jwdnd.course4.ecommerce;

import com.udacity.jwdnd.course4.ecommerce.user.CreateUserRequest;
import com.udacity.jwdnd.course4.ecommerce.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testCreateUser() {
        createUser("Alex", "alexPassword");
    }

    @Test
    public void testCreateUserPasswordConfirmationMismatch() {
        webTestClient.post().uri("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest("Alex", "alexPassword", "wrongPassword"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().isEmpty();
    }

    @Test
    public void testCreateUserPasswordComplexityInsufficient() {
        webTestClient.post().uri("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest("Alex", "1234", "1234"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().isEmpty();
    }

    @Test
    public void testCreateUserUsernameAlreadyTaken() {
        createUser("Alex", "alexPassword");

        webTestClient.post().uri("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest("Alex", "anotherPassword", "anotherPassword"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().isEmpty();
    }

    @Test
    public void testFindUserById() {
        Long userId = createUser("Alex", "alexPassword").getId();

        webTestClient.get().uri("/api/user/id/{id}", userId)
                .header("Authorization", "Basic "+ Base64Utils.encodeToString(("Alex" + ":" + "alexPassword").getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(User.class).value(user -> {
                        Assertions.assertEquals(userId, user.getId());
                        Assertions.assertEquals("Alex", user.getUsername());
                        Assertions.assertNull(user.getPassword());
                });
    }

    @Test
    public void testFindUserByUsername() {
        Long userId = createUser("Alex", "alexPassword").getId();

        webTestClient.get().uri("/api/user/{username}", "Alex")
                .header("Authorization", "Basic "+ Base64Utils.encodeToString(("Alex" + ":" + "alexPassword").getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(User.class).value(user -> {
                        Assertions.assertEquals(userId, user.getId());
                        Assertions.assertEquals("Alex", user.getUsername());
                        Assertions.assertNull(user.getPassword());
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
}
