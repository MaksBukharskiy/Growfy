package com.example.interaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class AuthRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email пользователя", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Пароль пользователя", example = "password123", required = true)
    private String password;
}