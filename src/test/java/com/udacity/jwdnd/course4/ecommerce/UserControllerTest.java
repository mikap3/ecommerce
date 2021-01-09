package com.udacity.jwdnd.course4.ecommerce;

import com.udacity.jwdnd.course4.ecommerce.dto.CreateUserRequest;
import com.udacity.jwdnd.course4.ecommerce.entity.User;
import com.udacity.jwdnd.course4.ecommerce.service.UserService;
import com.udacity.jwdnd.course4.ecommerce.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    public static class CreateUserRequestMatcher implements ArgumentMatcher<CreateUserRequest> {

        private final CreateUserRequest left;

        public CreateUserRequestMatcher(CreateUserRequest left) {
            this.left = left;
        }

        @Override
        public boolean matches(CreateUserRequest right) {
            return left.getUsername().equals(right.getUsername()) &&
                    left.getPassword().equals(right.getPassword()) &&
                    left.getConfirmPassword().equals(right.getConfirmPassword());
        }
    }

    @BeforeEach
    public void init() {
        CreateUserRequest mockCreateUserRequest = new CreateUserRequest();
        mockCreateUserRequest.setUsername("test");
        mockCreateUserRequest.setPassword("testPassword");
        mockCreateUserRequest.setConfirmPassword("testPassword");
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test");
        mockUser.setPassword("hashedTestPassword");
//        doReturn(mockUser).when(userService).createUser(any(CreateUserRequest.class));
        doReturn(mockUser).when(userService).createUser(argThat(new CreateUserRequestMatcher(mockCreateUserRequest)));
    }

    @Test
    public void testCreateUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashedTestPassword", user.getPassword());
    }
}
