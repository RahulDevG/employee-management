package com.emp.management.repository;

import com.emp.management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByMandatoryTrue();
}
