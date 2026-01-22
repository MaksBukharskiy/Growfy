package com.example.interaction.dto;

import com.example.interaction.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ на аутентификацию")
public class AuthResponse {

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Email пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Роль пользователя")
    private UserRole role;

    @Schema(description = "Сообщение", example = "Authentication successful")
    private String message;

    @Schema(description = "URL для редиректа", example = "/hr-dashboard")
    private String redirectTo;

    @Schema(description = "ID пользователя", example = "507f1f77bcf86cd799439011")
    private String userId;

    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Петров")
    private String lastName;

    public AuthResponse(String token, String email, UserRole role, String redirectTo) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.message = "Authentication successful";
        this.redirectTo = redirectTo;
    }

    public AuthResponse(String token, String email, UserRole role, String redirectTo,
                        String userId, String firstName, String lastName) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.message = "Authentication successful";
        this.redirectTo = redirectTo;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}