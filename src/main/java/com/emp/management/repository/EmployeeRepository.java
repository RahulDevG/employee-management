package com.emp.management.repository;

import com.emp.management.entity.Department;
import com.emp.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByName(@Param("name") String name);
    List<Employee> findByDepartments(@Param("departments") List<Department> departments);
}
