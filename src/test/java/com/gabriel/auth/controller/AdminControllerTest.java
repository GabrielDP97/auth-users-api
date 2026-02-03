package com.gabriel.auth.controller;

import com.gabriel.auth.domain.Role;
import com.gabriel.auth.domain.User;
import com.gabriel.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminController adminController;

    @Test
    void getAllUsers_shouldReturnListOfUsers() {

        // given
        User user1 = new User(
                "admin",
                "admin@test.com",
                "encodedPassword",
                Role.ADMIN
        );

        User user2 = new User(
                "user",
                "user@test.com",
                "encodedPassword",
                Role.USER
        );

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        // when
        List<User> users = adminController.getAllUsers();

        // then
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("admin@test.com", users.get(0).getEmail());
        assertEquals("user@test.com", users.get(1).getEmail());

        verify(userRepository).findAll();
    }
}

