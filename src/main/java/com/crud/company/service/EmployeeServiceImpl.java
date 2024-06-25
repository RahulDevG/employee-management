package com.crud.company.service;

import com.crud.company.Exception.ResourceNotFoundException;
import com.crud.company.entity.Department;
import com.crud.company.entity.Employee;
import com.crud.company.enums.ErrorMessage;
import com.crud.company.repository.DepartmentRepository;
import com.crud.company.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee addEmployee(Employee employee) {
        assignMandatoryDepartments(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        validateUpdate(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        validateDelete(id);
        employeeRepository.deleteById(id);
    }

    private void assignMandatoryDepartments(Employee employee) {
        List<Department> allByMandatoryTrue = departmentRepository.findAllByMandatoryTrue();
        if (CollectionUtils.isEmpty(employee.getDepartments())) {
            employee.setDepartments(allByMandatoryTrue);
        } else {
            List<Department> missingMandatoryTrue = allByMandatoryTrue.stream()
                    .filter(mandatory -> employee.getDepartments().stream()
                            .noneMatch(department -> department.getId().equals(mandatory.getId()))).toList();
            employee.getDepartments().addAll(missingMandatoryTrue);
        }
    }

    private void validateUpdate(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new ResourceNotFoundException(ErrorMessage.RecordDoesNotExist.getMessage());
        }
    }

    private void validateDelete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorMessage.RecordDoesNotExist.getMessage());
        }
    }
}
