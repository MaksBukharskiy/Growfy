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
@RequestMapping("/api/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
@CrossOrigin(origins = "*")
@Tag(name = "Панель сотрудника", description = "API для сотрудников")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    @Operation(
            summary = "Дашборд сотрудника",
            description = "Получение главной панели для сотрудника"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение дашборда")
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Welcome to Employee Dashboard! (Экран 2 для сотрудника)");
    }

    @Operation(
            summary = "Профиль сотрудника",
            description = "Получение информации о профиле сотрудника"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение профиля")
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Employee Profile Information");
    }

    @Operation(
            summary = "Задачи сотрудника",
            description = "Получение списка задач сотрудника"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение задач")
    @GetMapping("/tasks")
    public ResponseEntity<String> getTasks() {
        return ResponseEntity.ok("Employee Tasks List");
    }

    @Operation(
            summary = "Персональная информация",
            description = "Получение персональной информации сотрудника"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение информации")
    @GetMapping("/my-info")
    public ResponseEntity<String> getMyInfo() {
        return ResponseEntity.ok("Employee personal information");
    }
}