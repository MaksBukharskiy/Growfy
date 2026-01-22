package com.example.interaction.configuration;

import com.example.interaction.model.Employee;
import com.example.interaction.model.HRManager;
import com.example.interaction.model.UserRole;
import com.example.interaction.repository.EmployeeRepository;
import com.example.interaction.repository.HRManagerRepository;
import com.example.interaction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final HRManagerRepository hrManagerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing MongoDB test data...");
        
        if (!userRepository.existsByEmail("hr@example.com")) {
            HRManager hrManager = new HRManager();
            hrManager.setEmail("hr@example.com");
            hrManager.setPassword(passwordEncoder.encode("hrpassword"));
            hrManager.setFirstName("Анна");
            hrManager.setLastName("Сидорова");
            hrManager.setDepartment("HR");
            hrManager.setAccessLevel(2);
            hrManager.setRole(UserRole.HR);
            hrManagerRepository.save(hrManager);
            System.out.println("✅ Created HR user: hr@example.com / hrpassword");
        }

        if (!userRepository.existsByEmail("employee@example.com")) {
            Employee employee = new Employee();
            employee.setEmail("employee@example.com");
            employee.setPassword(passwordEncoder.encode("emppassword"));
            employee.setFirstName("Иван");
            employee.setLastName("Петров");
            employee.setPosition("Разработчик");
            employee.setDepartment("IT");
            employee.setHireDate(LocalDate.now());
            employee.setPhoneNumber("+79991234567");
            employee.setRole(UserRole.EMPLOYEE);
            employeeRepository.save(employee);
            System.out.println("✅ Created Employee user: employee@example.com / emppassword");
        }

        createTestUser("manager@example.com", "IT Manager", "IT", "Менеджер");
        createTestUser("analyst@example.com", "Бизнес-аналитик", "Аналитика", "Аналитик");

        System.out.println("MongoDB test data initialization completed!");
    }

    private void createTestUser(String email, String position, String department, String firstName) {
        if (!userRepository.existsByEmail(email)) {
            Employee employee = new Employee();
            employee.setEmail(email);
            employee.setPassword(passwordEncoder.encode("test123"));
            employee.setFirstName(firstName);
            employee.setLastName("Тестовый");
            employee.setPosition(position);
            employee.setDepartment(department);
            employee.setHireDate(LocalDate.now().minusMonths(6));
            employee.setPhoneNumber("+79990000000");
            employee.setRole(UserRole.EMPLOYEE);
            employeeRepository.save(employee);
            System.out.println("✅ Created test user: " + email);
        }
    }
}