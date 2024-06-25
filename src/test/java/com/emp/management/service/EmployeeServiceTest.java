package com.emp.management.service;

import com.emp.management.Exception.ResourceNotFoundException;
import com.emp.management.entity.Department;
import com.emp.management.entity.Employee;
import com.emp.management.enums.ErrorMessage;
import com.emp.management.repository.DepartmentRepository;
import com.emp.management.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    public static final long ID_DEPARTMENT_EXISTS_MANDATORY = 1L;
    public static final String NAME_DEPARTMENT_EXISTS_MANDATORY = "Organisation";
    public static final long ID_DEPARTMENT_EXISTS_NON_MANDATORY = 2L;
    public static final String NAME_DEPARTMENT_EXISTS_NON_MANDATORY = "Marketing";

    public static final Long ID_EMPLOYEE_EXISTS = 1L;
    public static final String NAME_EMPLOYEE_EXISTS = "Employee1";
    public static final Long ID_EMPLOYEE_NEW = 2L;
    public static final String NAME_EMPLOYEE_NEW = "Employee2";
    public static final Long ID_EMPLOYEE_EXISTS_DUPLICATE = 3L;

    @Mock
    private EmployeeRepository mockEmployeeRepository;

    @Mock
    private DepartmentRepository mockDepartmentRepository;

    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;

    private EmployeeService ref;

    @InjectMocks
    private EmployeeServiceImpl concreteRef;

    private Employee employeeUpdatedWithoutDepartment;
    private Employee employeeNewWithoutDepartment;
    private Employee employeeNewWithoutMandatoryDepartment;

    private List<Department> mandatoryDepartments = new ArrayList<>();
    private List<Department> nonMandatoryDepartments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);

        ref = concreteRef;

        mandatoryDepartments.add(Department.builder().id(ID_DEPARTMENT_EXISTS_MANDATORY)
                .name(NAME_DEPARTMENT_EXISTS_MANDATORY).readOnly(false).mandatory(true).build());
        nonMandatoryDepartments.add(Department.builder().id(ID_DEPARTMENT_EXISTS_NON_MANDATORY)
                .name(NAME_DEPARTMENT_EXISTS_NON_MANDATORY).readOnly(false).mandatory(false).build());

        employeeUpdatedWithoutDepartment = Employee.builder().id(ID_EMPLOYEE_EXISTS).name(NAME_EMPLOYEE_EXISTS).build();
        employeeNewWithoutDepartment = Employee.builder().id(ID_EMPLOYEE_NEW).name(NAME_EMPLOYEE_NEW).build();
        employeeNewWithoutMandatoryDepartment = Employee.builder().id(ID_EMPLOYEE_NEW).name(NAME_EMPLOYEE_NEW)
                .departments(nonMandatoryDepartments).build();

        lenient().doReturn(Collections.singletonList(employeeUpdatedWithoutDepartment)).when(mockEmployeeRepository).findAll();
        lenient().doReturn(true).when(mockEmployeeRepository).existsById(ID_EMPLOYEE_EXISTS);
        lenient().doReturn(true).when(mockEmployeeRepository).existsById(ID_EMPLOYEE_EXISTS_DUPLICATE);

        lenient().doReturn(mandatoryDepartments).when(mockDepartmentRepository).findAllByMandatoryTrue();
    }

    @AfterEach
    void tearDown() {
        mockEmployeeRepository = null;
        mockDepartmentRepository = null;
        concreteRef = null;
        ref = null;

        employeeUpdatedWithoutDepartment = null;
        employeeNewWithoutDepartment = null;
        employeeNewWithoutMandatoryDepartment = null;
        mandatoryDepartments = null;
        nonMandatoryDepartments = null;

        employeeArgumentCaptor = null;
    }

    @Test
    void testAddEmployees_employee_withoutDepartment_success() {
        assertDoesNotThrow(() -> ref.addEmployee(employeeNewWithoutDepartment));
        Mockito.verify(mockEmployeeRepository).save(employeeArgumentCaptor.capture());
        Employee actual = employeeArgumentCaptor.getValue();
        assertTrue(actual.getDepartments().stream().anyMatch(Department::getMandatory));
    }

    @Test
    void testAddEmployee_employee_withoutMandatoryDepartment_success() {
        assertDoesNotThrow(() -> ref.addEmployee(employeeNewWithoutMandatoryDepartment));
        Mockito.verify(mockEmployeeRepository).save(employeeArgumentCaptor.capture());
        Employee actual = employeeArgumentCaptor.getValue();
        assertTrue(actual.getDepartments().stream().anyMatch(Department::getMandatory));
    }

    @Test
    void testUpdateEmployee_employee_doesNotExist_resourceNotFoundException() {
        ResourceNotFoundException actual =
                assertThrows(ResourceNotFoundException.class, () -> ref.updateEmployee(employeeNewWithoutDepartment));
        Assertions.assertEquals(ErrorMessage.RecordDoesNotExist.getMessage(), actual.getMessage());
    }

    @Test
    void testUpdateEmployees_success() {
        assertDoesNotThrow(() -> ref.updateEmployee(employeeUpdatedWithoutDepartment));
    }

    @Test
    void testDeleteEmployee_employee_doesNotExist_resourceNotFoundException() {
        ResourceNotFoundException actual =
                assertThrows(ResourceNotFoundException.class, () -> ref.deleteEmployee(ID_EMPLOYEE_NEW));
        assertEquals(ErrorMessage.RecordDoesNotExist.getMessage(), actual.getMessage());
    }

    @Test
    void testDeleteEmployee_success() {
        assertDoesNotThrow(() -> ref.deleteEmployee(ID_EMPLOYEE_EXISTS));
    }
}