package com.example.management.controller;

import com.example.management.entity.Tasks;
import com.example.management.entity.User;
import com.example.management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Tag(name = "User", description = "Interaction with users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create user"
    )
    @PostMapping(path = "user/create")
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @Operation(
            summary = "Get user by email"
    )
    @GetMapping(path = "user/get/by/email")
    public User getByUserEmail(@RequestParam String email) {
        return userService.getByUserEmail(email);
    }

}
