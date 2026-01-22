package com.example.interaction.restController;

import com.example.interaction.dto.AuthRequest;
import com.example.interaction.dto.AuthResponse;
import com.example.interaction.dto.RegisterEmployeeRequest;
import com.example.interaction.dto.RegisterHRRequest;
import com.example.interaction.model.User;
import com.example.interaction.model.UserRole;
import com.example.interaction.secutiry.JwtUtil;
import com.example.interaction.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Аутентификация", description = "API для регистрации и входа пользователей")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Operation(
            summary = "Вход в систему",
            description = "Аутентификация пользователя и получение JWT токена"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная аутентификация",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Неверные учетные данные"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Данные для входа", required = true)
            @Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();

            String token = jwtUtil.generateToken(user);

            String redirectTo = user.getRole() == UserRole.HR ? "/hr-dashboard" : "/employee-dashboard";

            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setEmail(user.getEmail());
            response.setRole(user.getRole());
            response.setRedirectTo(redirectTo);
            response.setMessage("Authentication successful");
            response.setUserId(user.getId());

            if (user instanceof com.example.interaction.model.Employee) {
                com.example.interaction.model.Employee emp = (com.example.interaction.model.Employee) user;
                response.setFirstName(emp.getFirstName());
                response.setLastName(emp.getLastName());
            } else if (user instanceof com.example.interaction.model.HRManager) {
                com.example.interaction.model.HRManager hr = (com.example.interaction.model.HRManager) user;
                response.setFirstName(hr.getFirstName());
                response.setLastName(hr.getLastName());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, "Invalid credentials", null, null, null, null));
        }
    }

    @Operation(
            summary = "Регистрация сотрудника",
            description = "Создание нового аккаунта сотрудника"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Сотрудник успешно зарегистрирован",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные или email уже существует"
            )
    })
    @PostMapping("/register/employee")
    public ResponseEntity<AuthResponse> registerEmployee(
            @Parameter(description = "Данные для регистрации сотрудника", required = true)
            @Valid @RequestBody RegisterEmployeeRequest request) {
        try {
            userService.registerEmployee(request);

            AuthRequest loginRequest = new AuthRequest();
            loginRequest.setEmail(request.getEmail());
            loginRequest.setPassword(request.getPassword());

            return login(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, e.getMessage(), null, null, null, null));
        }
    }

    @Operation(
            summary = "Регистрация HR-менеджера",
            description = "Создание нового аккаунта HR-менеджера"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HR-менеджер успешно зарегистрирован",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные или email уже существует"
            )
    })
    @PostMapping("/register/hr")
    public ResponseEntity<AuthResponse> registerHR(
            @Parameter(description = "Данные для регистрации HR-менеджера", required = true)
            @Valid @RequestBody RegisterHRRequest request) {
        try {
            userService.registerHRManager(request);

            AuthRequest loginRequest = new AuthRequest();
            loginRequest.setEmail(request.getEmail());
            loginRequest.setPassword(request.getPassword());

            return login(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, e.getMessage(), null, null, null, null));
        }
    }

    @Operation(
            summary = "Валидация JWT токена",
            description = "Проверка валидности JWT токена и получение информации из него"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Токен валиден",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Токен невалиден или просрочен"
            )
    })
    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(
            @Parameter(description = "Токен для валидации", required = true)
            @RequestBody Map<String, String> request) {
        String token = request.get("token");

        try {
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractUserRole(token);
            String userId = jwtUtil.extractUserId(token);
            String issuer = jwtUtil.extractIssuer(token);
            String audience = jwtUtil.extractAudience(token);

            if (!"ups18".equals(issuer)) {
                throw new RuntimeException("Invalid token issuer");
            }

            if (!"everyone".equals(audience)) {
                throw new RuntimeException("Invalid token audience");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("username", username);
            response.put("role", role);
            response.put("userId", userId);
            response.put("issuer", issuer);
            response.put("audience", audience);
            response.put("expiration", jwtUtil.extractExpiration(token));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @Operation(
            summary = "Проверка здоровья сервиса",
            description = "Endpoint для проверки работоспособности сервиса аутентификации"
    )
    @ApiResponse(responseCode = "200", description = "Сервис работает")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("✅ Auth service is running with MongoDB");
    }
}