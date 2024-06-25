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
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootTest
public class EmployeeControllerIT {
    public static final String NAME_EMPLOYEE_NEW = "Employee";
    public static final String NAME_EMPLOYEE_UPDATED = "EmployeeUpdated";
    @Autowired
    private EmployeeController employeeController;

    private Employee employeeDummy;
    private Employee employeeNew;
    private Employee employeeDuplicate;
    private Employee employeeUpdated;

    @BeforeEach
    void setUp() {
        employeeDummy = Employee.builder().build();
        employeeNew = Employee.builder().name(NAME_EMPLOYEE_NEW).build();
        employeeDuplicate = Employee.builder().name(NAME_EMPLOYEE_NEW).build();
        employeeUpdated = Employee.builder().name(NAME_EMPLOYEE_UPDATED).build();
    }
    @AfterEach
    void tearDown() {
        employeeDummy = null;
        employeeNew = null;
        employeeDuplicate = null;
        employeeUpdated = null;

        employeeController = null;
    }

    @Test
    public void testCRUD_employee()
    {
        addEmployee();
        getAllEmployees();
        updateEmployee();
        deleteEmployee();
    }

    private void addEmployee() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> employeeController.addEmployee(employeeDummy));

        ResponseEntity<Employee> responseEntity = Assertions.assertDoesNotThrow(() -> employeeController.addEmployee(employeeNew));
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getBody().getDepartments().stream().anyMatch(Department::getMandatory));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> employeeController.addEmployee(employeeDuplicate));
        employeeUpdated.setId(responseEntity.getBody().getId());
    }

    public void getAllEmployees() {
        ResponseEntity<List<Employee>> responseEntity = employeeController.getEmployees();
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertFalse(responseEntity.getBody().isEmpty());
        Assertions.assertTrue(responseEntity.getBody().stream().noneMatch(employee -> CollectionUtils.isEmpty(employee.getDepartments())));
    }

    public void updateEmployee() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> employeeController.updateEmployee(employeeDummy));

        ResponseEntity<Employee> actual = Assertions.assertDoesNotThrow(() -> employeeController.updateEmployee(employeeUpdated));
        Assertions.assertNotNull(actual.getBody());
        Assertions.assertEquals(employeeUpdated.getName(), actual.getBody().getName());
        Assertions.assertNull(actual.getBody().getDepartments());
    }

    public void deleteEmployee() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> employeeController.deleteEmployee(null));
        Assertions.assertDoesNotThrow(() -> employeeController.deleteEmployee(employeeUpdated.getId()));
        ResponseEntity<List<Employee>> responseEntity = employeeController.getEmployees();
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getBody().isEmpty());
    }
}
