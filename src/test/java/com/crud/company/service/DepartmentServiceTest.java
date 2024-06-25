package com.crud.company.service;

import com.crud.company.Exception.BusinessValidationException;
import com.crud.company.Exception.ResourceNotFoundException;
import com.crud.company.entity.Department;
import com.crud.company.enums.ErrorMessage;
import com.crud.company.repository.DepartmentRepository;
import com.crud.company.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    public static final long ID_DEPARTMENT_EXISTS = 1L;
    public static final String NAME_DEPARTMENT_EXISTS = "Department1";

    private static final Long ID_DEPARTMENT_NEW = 2L;
    private static final String NAME_DEPARTMENT_NEW = "Department2";

    public static final Long ID_DEPARTMENT_EXISTS_READ_ONLY = 3L;
    public static final String NAME_DEPARTMENT_EXISTS_READ_ONLY = "Department3";

    @Mock
    private DepartmentRepository mockDepartmentRepository;
    @Mock
    private EmployeeRepository mockEmployeeRepository;
    private DepartmentService ref;
    @InjectMocks
    private DepartmentServiceImpl concreteRef;

    private Department departmentExists;
    private Department departmentExistsReadOnly;
    private Department departmentNewMandatory;

    @BeforeEach
    void setUp() {
        ref = concreteRef;
        departmentExists = Department.builder().id(ID_DEPARTMENT_EXISTS).name(NAME_DEPARTMENT_EXISTS).readOnly(false).mandatory(true).build();
        departmentExistsReadOnly = Department.builder().id(ID_DEPARTMENT_EXISTS_READ_ONLY).name(NAME_DEPARTMENT_EXISTS_READ_ONLY).readOnly(true).build();
        departmentNewMandatory = Department.builder().id(ID_DEPARTMENT_NEW).name(NAME_DEPARTMENT_NEW).mandatory(true).build();

        lenient().doReturn(Collections.singletonList(departmentExists)).when(mockDepartmentRepository).findAll();
        lenient().doReturn(Optional.of(departmentExists)).when(mockDepartmentRepository).findById(ID_DEPARTMENT_EXISTS);
        lenient().doReturn(Optional.of(departmentExistsReadOnly)).when(mockDepartmentRepository).findById(ID_DEPARTMENT_EXISTS_READ_ONLY);

        lenient().doReturn(Collections.emptyList()).when(mockEmployeeRepository).findByDepartments(Mockito.anyList());
    }

    @AfterEach
    void tearDown() {
        ref = null;
        concreteRef = null;
        mockDepartmentRepository = null;
        mockEmployeeRepository = null;

        departmentExists = null;
        departmentExistsReadOnly = null;
        departmentNewMandatory = null;
    }

    @Test
    void testAddDepartment_success() {
        assertDoesNotThrow(() -> ref.addDepartment(departmentNewMandatory));
    }

    @Test
    void testUpdateDepartment_departmentDoesNotExists_resourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> ref.updateDepartment(departmentNewMandatory));
    }

    @Test
    void testUpdateDepartment_departmentIsReadOnly_businessValidationException() {
        BusinessValidationException actual = assertThrows(BusinessValidationException.class, () -> ref.updateDepartment(departmentExistsReadOnly));
        assertEquals(ErrorMessage.CannotModifyReadOnlyRecord.getMessage(), actual.getMessage());
    }

    @Test
    void testUpdateDepartment_success() {
        assertDoesNotThrow(() -> ref.updateDepartment(departmentExists));
    }

    @Test
    void testDeleteDepartment_departmentDoesNotExists_resourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> ref.deleteDepartment(ID_DEPARTMENT_NEW));
    }

    @Test
    void testDeleteDepartment_departmentIsReadOnly_businessValidationException() {
        BusinessValidationException actual = assertThrows(BusinessValidationException.class, () -> ref.deleteDepartment(ID_DEPARTMENT_EXISTS_READ_ONLY));
        assertEquals(ErrorMessage.CannotDeleteReadOnlyRecord.getMessage(), actual.getMessage());
    }

    @Test
    void testDeleteDepartment_success() {
        assertDoesNotThrow(() -> ref.deleteDepartment(ID_DEPARTMENT_EXISTS));
    }
}