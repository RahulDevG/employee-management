package com.emp.management.service;

import com.emp.management.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department addDepartment(Department department);
    Department updateDepartment(Department department);
    void deleteDepartment(Long id);
}
