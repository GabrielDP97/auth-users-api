package com.gabriel.auth.service;

import com.gabriel.auth.domain.Role;
import com.gabriel.auth.domain.User;
import com.gabriel.auth.exception.EmailAlreadyExistsException;
import com.gabriel.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(String username, String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = new User(
                username,
                email,
                password, // ⚠️ luego lo encriptaremos
                Role.USER
        );

        return userRepository.save(user);
    }
}

