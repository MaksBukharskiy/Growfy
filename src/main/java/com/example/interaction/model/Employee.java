package com.example.interaction.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "employees")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User {
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private LocalDate hireDate;
    private String phoneNumber;
}