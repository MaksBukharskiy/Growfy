package com.example.interaction.service;

import com.example.interaction.dto.RegisterEmployeeRequest;
import com.example.interaction.dto.RegisterHRRequest;
import com.example.interaction.model.Employee;
import com.example.interaction.model.HRManager;
import com.example.interaction.model.User;
import com.example.interaction.model.UserRole;
import com.example.interaction.repository.EmployeeRepository;
import com.example.interaction.repository.HRManagerRepository;
import com.example.interaction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final HRManagerRepository hrManagerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public Employee registerEmployee(RegisterEmployeeRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Employee employee = new Employee();
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPosition(request.getPosition());
        employee.setDepartment(request.getDepartment());
        employee.setHireDate(request.getHireDate() != null ? request.getHireDate() : LocalDate.now());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setRole(UserRole.EMPLOYEE);

        return employeeRepository.save(employee);
    }

    public HRManager registerHRManager(RegisterHRRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        HRManager hrManager = new HRManager();
        hrManager.setEmail(request.getEmail());
        hrManager.setPassword(passwordEncoder.encode(request.getPassword()));
        hrManager.setFirstName(request.getFirstName());
        hrManager.setLastName(request.getLastName());
        hrManager.setDepartment(request.getDepartment());
        hrManager.setAccessLevel(request.getAccessLevel() != null ? request.getAccessLevel() : 1);
        hrManager.setRole(UserRole.HR);

        return hrManagerRepository.save(hrManager);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<HRManager> getAllHRManagers() {
        return hrManagerRepository.findAll();
    }
}