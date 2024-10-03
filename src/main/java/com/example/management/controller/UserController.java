package com.example.management.controller;

import com.example.management.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Interaction with users")
@ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Внутренняя ошибка сервера")
public interface UserController {

    @Operation(
            summary = "Create user"
    )
    @PostMapping(path = "/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK, description = "Запрос выполнен успешно",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "" + HttpServletResponse.SC_BAD_REQUEST,
                    description = """
                            * Имя не передано
                            * Email не передан или имеет менее 5 символов
                            * Email уже существует
                            * Пароль не передан или имеет менее 8 символов
                            """)
    })
    User create(@RequestBody User user);

    @Operation(
            summary = "Get user by email"
    )
    @GetMapping(path = "/get/by/email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK, description = "Запрос выполнен успешно",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND,
                    description = "Пользователь не найден")
    })
    User getByUserEmail(@RequestParam String email);
}
