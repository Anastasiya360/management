package com.example.management.service;

import com.example.management.entity.User;
import com.example.management.exceptoin.ApiException;
import com.example.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        user.setRole("admin");
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ApiException("Имя не передано", 400);
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().length() < 5) {
            throw new ApiException("Email не передан или имеет менее 5 символов", 400);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ApiException("Email уже существует", 400);
        }
        if (user.getPassword() == null || user.getPassword().isBlank() || user.getPassword().length() < 8) {
            throw new ApiException("Пароль не передан или имеет менее 8 символов", 400);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUserEmail(String email) {
        if (userRepository.findUserByEmail(email) != null) {
            return userRepository.findUserByEmail(email);
        } else {
            throw new ApiException("Пользователь не найден", 404);
        }
    }

    /**
     * Получение пользователя по email для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUserEmail;
    }
}
