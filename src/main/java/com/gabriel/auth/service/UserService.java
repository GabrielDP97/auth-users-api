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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User authenticate(String email, String password) {

        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }



    @Transactional
    public User registerUser(String username, String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = new User(
                username,
                email,
                passwordEncoder.encode(password), // 🔐 AQUÍ
                Role.USER
        );

        return userRepository.save(user);
    }

}

