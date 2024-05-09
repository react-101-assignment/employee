package com.example.employee.management.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PostLoad;
import javax.persistence.PreUpdate;
import lombok.Getter;

@Getter
@Document(collection = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private int age;
    private String mobile;
    private String email;
    private String department;
    private String address;
    private boolean isActive=true;

    @PostLoad
    @PreUpdate
    private void updateFullName() {
        StringBuilder fullNameBuilder = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            fullNameBuilder.append(firstName);
        }
        if (middleName != null && !middleName.isEmpty()) {
            fullNameBuilder.append(" ").append(middleName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            fullNameBuilder.append(" ").append(lastName);
        }
        fullName = fullNameBuilder.toString();
    }

    public void deleteEmployee() {
        this.isActive = false;
    }
}
