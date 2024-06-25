package com.crud.company.controller;

import com.crud.company.entity.Department;
import com.crud.company.service.DepartmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/department")
@Validated
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<Department>> getDepartments() {
        return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Department> addDepartment(@RequestBody @NotNull @Valid Department department) {
        return new ResponseEntity<>(departmentService.addDepartment(department), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Department> updateDepartment(@RequestBody @NotNull @Valid Department department) {
        return new ResponseEntity<>(departmentService.updateDepartment(department), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteDepartment(@PathVariable @NotNull Long id) {
        departmentService.deleteDepartment(id);
    }
}
