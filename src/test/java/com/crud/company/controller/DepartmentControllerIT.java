package com.crud.company.controller;

import com.crud.company.entity.Department;
import com.crud.company.entity.Employee;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SpringBootTest
public class DepartmentControllerIT {
    public static final String NAME_EMPLOYEE_NEW = "Employee";
    public static final String NAME_DEPARTMENT_MANDATORY = "Organisation";
    public static final long ID_DEPARTMENT_MANDATORY = 1L;
    public static final String NAME_DEPARTMENT_NEW = "Marketing";
    @Autowired
    private DepartmentController departmentController;
    @Autowired
    private EmployeeController employeeController;

    private List<Department> departmentMandatoryExists;
    private Department departmentDummy;
    private Department departmentNew;
    private Department departmentUpdated;

    private Employee employeeNewWithMandatoryDepartment;

    @BeforeEach
    void setUp() {
        departmentDummy = Department.builder().build();
        departmentNew = Department.builder().name(NAME_DEPARTMENT_NEW).readOnly(true).mandatory(true).build();
        departmentUpdated = Department.builder().name(NAME_DEPARTMENT_NEW).readOnly(false).mandatory(false).build();
        departmentMandatoryExists = Collections.singletonList(Department.builder()
                .name(NAME_DEPARTMENT_MANDATORY).id(ID_DEPARTMENT_MANDATORY).mandatory(true)
                .build());

        employeeNewWithMandatoryDepartment = Employee.builder().name(NAME_EMPLOYEE_NEW)
                .departments(departmentMandatoryExists).build();

        addEmployee();
    }

    @AfterEach
    void tearDown() {
        deleteEmployee();

        departmentController = null;
        employeeController = null;

        departmentDummy = null;
        departmentNew = null;
        departmentUpdated = null;
        departmentMandatoryExists = null;
        employeeNewWithMandatoryDepartment = null;
    }

    @Test
    public void testCRUD_department() {
        addDepartment();
        updateDepartment();
        getAllDepartments();
        deleteDepartment();
    }

    private void addEmployee() {
        Assertions.assertDoesNotThrow(() -> employeeController.addEmployee(employeeNewWithMandatoryDepartment));
    }

    public void getAllDepartments() {
        ResponseEntity<List<Department>> responseEntity = departmentController.getDepartments();
        assert (responseEntity.getBody() != null);
        assert (responseEntity.getBody().stream().anyMatch(Department::getMandatory));
    }

    public void addDepartment() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> departmentController.addDepartment(departmentDummy));

        Department actual = departmentController.addDepartment(departmentNew).getBody();
        assert (actual != null);
        Assertions.assertEquals(NAME_DEPARTMENT_NEW, actual.getName());

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> departmentController.addDepartment(departmentUpdated));
        departmentUpdated.setId(actual.getId());
    }

    public void updateDepartment() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                departmentController.updateDepartment(departmentDummy));

        Department output = departmentController.updateDepartment(departmentUpdated).getBody();
        assert (output != null);
        Assertions.assertFalse(output.getReadOnly());
        Assertions.assertFalse(output.getMandatory());
        ResponseEntity<List<Employee>> employees = employeeController.getEmployees();
        assert (employees.getBody() != null);
        Assertions.assertTrue(employees.getBody().stream().allMatch(employee -> employee.getDepartments().size() == 1));
    }

    public void deleteDepartment() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> departmentController.deleteDepartment(null));
        Assertions.assertDoesNotThrow(() -> departmentController.deleteDepartment(departmentUpdated.getId()));
    }

    private void deleteEmployee() {
        Objects.requireNonNull(employeeController.getEmployees().getBody()).stream()
                .filter(employee -> employee.getName().equals(NAME_EMPLOYEE_NEW)).findFirst()
                .ifPresent(employee ->
                        Assertions.assertDoesNotThrow(() -> employeeController.deleteEmployee(employee.getId())));
    }
}
