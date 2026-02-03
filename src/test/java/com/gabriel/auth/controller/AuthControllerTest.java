package com.gabriel.auth.controller;

import com.gabriel.auth.domain.Role;
import com.gabriel.auth.domain.User;
import com.gabriel.auth.dto.LoginRequest;
import com.gabriel.auth.dto.LoginResponse;
import com.gabriel.auth.dto.RegisterRequest;
import com.gabriel.auth.dto.UserResponse;
import com.gabriel.auth.security.JwtService;
import com.gabriel.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_shouldReturnJwtToken() {

        // given
        LoginRequest request = mock(LoginRequest.class);
        when(request.getEmail()).thenReturn("test@test.com");
        when(request.getPassword()).thenReturn("123456");

        User user = new User(
                "test",
                "test@test.com",
                "encodedPassword",
                Role.USER
        );

        when(userService.authenticate("test@test.com", "123456"))
                .thenReturn(user);

        when(jwtService.generateToken(user))
                .thenReturn("FAKE_JWT");

        // when
        ResponseEntity<LoginResponse> response =
                authController.login(request);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("FAKE_JWT", response.getBody().getAccessToken());
    }

    @Test
    void register_shouldCreateUserAndReturnUserResponse() {

        // given
        RegisterRequest request = mock(RegisterRequest.class);
        when(request.getUsername()).thenReturn("test");
        when(request.getEmail()).thenReturn("test@test.com");
        when(request.getPassword()).thenReturn("123456");

        User user = new User(
                "test",
                "test@test.com",
                "encodedPassword",
                Role.USER
        );

        when(userService.register("test", "test@test.com", "123456"))
                .thenReturn(user);

        // when
        ResponseEntity<UserResponse> response =
                authController.register(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test@test.com", response.getBody().getEmail());
        assertEquals(Role.USER, response.getBody().getRole());
    }

    @Test
    void me_shouldReturnAuthenticatedUser() {

        // given
        User user = new User(
                "test",
                "test@test.com",
                "encodedPassword",
                Role.USER
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        // when
        ResponseEntity<UserResponse> response =
                authController.me(authentication);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@test.com", response.getBody().getEmail());
    }
}


