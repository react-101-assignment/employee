package com.example.employee.management.controller;

import com.example.employee.management.model.Employee;
import com.example.employee.management.repository.EmployeeRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
getAllEmployee  - Done
postEmployee    - Done
getEmployee     - Done
deleteEmployee  - Done
updateEmployee  - Done

 */


@RestController
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employee")
    public List<Employee> getAllEmployee() {
        List<Employee> allEmployees = employeeRepository.findAll();

        return allEmployees.stream()
                .filter(Employee::isActive)
                .collect(Collectors.toList());
    }

    @PostMapping("/employee")
    public Employee postEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee with ID " + id + " not found.");
        }
        Optional<Employee> employee = employeeRepository.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @PostMapping("/employee/delete/{id}")
    public Employee deleteEmployee(@PathVariable String id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        employee.deleteEmployee();
        return employeeRepository.save(employee);
    }

    @PatchMapping("/employee/update/{id}")
    public Employee updateEmployee(@RequestBody Employee updatedEmployee, @PathVariable String id) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        Field[] fields = updatedEmployee.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(updatedEmployee);

                if (!field.getName().equals("isActive") && value != null) {
                    field.set(existingEmployee, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return employeeRepository.save(existingEmployee);
    }
}
