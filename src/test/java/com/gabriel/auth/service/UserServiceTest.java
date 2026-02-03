package com.gabriel.auth.service;

import com.gabriel.auth.domain.Role;
import com.gabriel.auth.domain.User;
import com.gabriel.auth.exception.EmailAlreadyExistsException;
import com.gabriel.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void register_shouldCreateUserWithEncryptedPassword() {
        // given
        String username = "test";
        String email = "test@test.com";
        String rawPassword = "123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        User savedUser = userService.register(username, email, rawPassword);

        // then
        assertNotNull(savedUser);
        assertEquals(email, savedUser.getEmail());
        assertEquals(Role.USER, savedUser.getRole());
        assertNotEquals(rawPassword, savedUser.getPassword());
        assertTrue(passwordEncoder.matches(rawPassword, savedUser.getPassword()));
    }

    @Test
    void register_shouldFailIfEmailAlreadyExists() {
        // given
        String email = "test@test.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(mock(User.class)));

        // then
        assertThrows(EmailAlreadyExistsException.class, () ->
                userService.register("test", email, "123456")
        );
    }

    @Test
    void authenticate_shouldReturnUserWhenCredentialsAreCorrect() {
        // given
        String email = "test@test.com";
        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User("test", email, encodedPassword, Role.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        User authenticated = userService.authenticate(email, rawPassword);

        // then
        assertNotNull(authenticated);
        assertEquals(email, authenticated.getEmail());
    }

    @Test
    void authenticate_shouldFailWithWrongPassword() {
        // given
        String email = "test@test.com";
        String encodedPassword = passwordEncoder.encode("correctPassword");

        User user = new User("test", email, encodedPassword, Role.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // then
        assertThrows(RuntimeException.class, () ->
                userService.authenticate(email, "wrongPassword")
        );
    }
}


