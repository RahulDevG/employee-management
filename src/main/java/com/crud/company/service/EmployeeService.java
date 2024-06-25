package com.crud.company.service;

import com.crud.company.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee addEmployee(Employee employee);
    Employee updateEmployee(Employee employee) throws Exception;
    void deleteEmployee(Long id) throws Exception;
}
