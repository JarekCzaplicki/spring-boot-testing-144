package org.example.springboottesting144.service.impl;

import org.example.springboottesting144.model.Employee;
import org.example.springboottesting144.service.EmployeeService;

import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public Employee saveEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return null;
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return Optional.empty();
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return null;
    }

    @Override
    public void deleteEmployee(long id) {

    }
}
