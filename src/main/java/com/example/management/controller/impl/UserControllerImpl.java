package com.example.management.controller.impl;

import com.example.management.controller.UserController;
import com.example.management.entity.User;
import com.example.management.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserServiceImpl userService;

    @Override
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @Override
    public User getByUserEmail(@RequestParam String email) {
        return userService.getByUserEmail(email);
    }

}
