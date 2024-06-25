package com.crud.company.service;

import com.crud.company.entity.Employee;
import com.crud.company.enums.ErrorMessage;
import com.crud.company.Exception.BusinessValidationException;
import com.crud.company.Exception.ResourceNotFoundException;
import com.crud.company.entity.Department;
import com.crud.company.repository.DepartmentRepository;
import com.crud.company.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Department department) {
        validateUpdate(department);
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        validateDelete(id);
        departmentRepository.deleteById(id);
    }

    private void validateUpdate(Department department) {
        Optional<Department> optionalDepartment = departmentRepository.findById(department.getId());
        if(optionalDepartment.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessage.RecordDoesNotExist.getMessage());
        }
        if (department.getReadOnly() && optionalDepartment.get().getReadOnly()) {
            throw new BusinessValidationException(ErrorMessage.CannotModifyReadOnlyRecord.getMessage());
        } else {
            department.setEmployees(optionalDepartment.get().getEmployees());
        }
    }

    private void validateDelete(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if(optionalDepartment.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessage.RecordDoesNotExist.getMessage());
        }
        if(optionalDepartment.get().getReadOnly())
        {
            throw new BusinessValidationException(ErrorMessage.CannotDeleteReadOnlyRecord.getMessage());
        }
        else
        {
            List<Employee> employees = employeeRepository.findByDepartments(Collections.singletonList(optionalDepartment.get()));
            employees.forEach(employee -> {
                employee.getDepartments().remove(optionalDepartment.get());
                employeeRepository.save(employee);
            });
        }
    }
}
