package com.crud.company.service;

import com.crud.company.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department addDepartment(Department department);
    Department updateDepartment(Department department);
    void deleteDepartment(Long id);
}
