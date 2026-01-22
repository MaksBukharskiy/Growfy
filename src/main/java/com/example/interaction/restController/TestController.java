package com.example.interaction.restController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
@Tag(name = "API для тестирования", description = "Публичные API для тестирования")
public class TestController {

    @Operation(
            summary = "Проверка здоровья приложения",
            description = "Проверка работы приложения и подключения к базе данных"
    )
    @ApiResponse(responseCode = "200", description = "Endpoint работает")
    @GetMapping("/endpoint/public")
    public ResponseEntity<String> getPublicEndpoint() {
        return ResponseEntity.ok("Public endpoint работает");
    }

    @Operation(
            summary = "Публичный endpoint",
            description = "Пример публичного endpoint, не требующего аутентификации"
    )
    @ApiResponse(responseCode = "200", description = "Успешный ответ")
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This is a public endpoint");
    }


}