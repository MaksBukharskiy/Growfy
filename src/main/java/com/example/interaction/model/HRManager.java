package com.example.interaction.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hr_managers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class HRManager extends User {
    private String firstName;
    private String lastName;
    private String department;
    private Integer accessLevel = 1;
}