package com.example.interaction.restController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr")
@PreAuthorize("hasRole('HR')")
@CrossOrigin(origins = "*")
@Tag(name = "HR панель", description = "API для HR-менеджеров")
@SecurityRequirement(name = "bearerAuth")
public class HRController {

    @Operation(
            summary = "Дашборд HR",
            description = "Получение главной панели управления для HR-менеджера"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение дашборда")
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Welcome to HR Dashboard! (Экран 1 для HR)");
    }

    @Operation(
            summary = "Список сотрудников",
            description = "Получение списка всех сотрудников компании"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение списка сотрудников")
    @GetMapping("/employees")
    public ResponseEntity<String> getAllEmployees() {
        return ResponseEntity.ok("List of all employees");
    }

    @Operation(
            summary = "Отчеты",
            description = "Получение отчетов и аналитики по персоналу"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение отчетов")
    @GetMapping("/reports")
    public ResponseEntity<String> getReports() {
        return ResponseEntity.ok("HR Reports and Analytics");
    }

    @Operation(
            summary = "Статистика",
            description = "Получение статистики по персоналу"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение статистики")
    @GetMapping("/statistics")
    public ResponseEntity<String> getStatistics() {
        return ResponseEntity.ok("HR Statistics");
    }
}