package com.gabriel.auth.service;

import com.gabriel.auth.domain.Role;
import com.gabriel.auth.domain.User;
import com.gabriel.auth.exception.EmailAlreadyExistsException;
import com.gabriel.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registro de usuario
     */
    @Transactional
    public User register(String username, String email, String rawPassword) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = new User(
                username,
                email,
                passwordEncoder.encode(rawPassword),
                Role.USER
        );

        return userRepository.save(user);
    }

    /**
     * Autenticación de usuario
     */
    public User authenticate(String email, String rawPassword) {

        return userRepository.findByEmail(email)
                .filter(user ->
                        passwordEncoder.matches(rawPassword, user.getPassword())
                )
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}

