import {Component, OnInit} from '@angular/core';
import {EmployeeService} from "./employee.service";
import {DepartmentService} from "../department/department.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DialogService} from "../confirmation-dialog/dialog.service";

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {
  errorMessage: string = null;
  employees: any;
  departments: any;
  employeeForm!: FormGroup;

  constructor(private employeeService: EmployeeService,
              private departmentService: DepartmentService,
              private dialogService: DialogService) {
  }

  ngOnInit() {
    this.getAllEmployee();
    this.getAllDepartments();
    this.resetForm();
  }

  resetForm() {
    this.errorMessage = null;
    this.employeeForm = new FormGroup({
      name: new FormControl('', Validators.required),
      departments: new FormControl([])
    });
  }

  populateFormData(employee: any) {
    this.errorMessage = null;
    this.employeeForm = new FormGroup({
      id: new FormControl(employee.id),
      name: new FormControl(employee.name, Validators.required),
      departments: new FormControl(this.buildDepartmentIdArray(employee.departments))
    });
  }

  buildDepartmentIdArray(dept: []) {
    let id: number[] = [];
    dept.forEach((ele: any) => {
      id.push(ele.id);
    })
    return id;
  }

  getAllEmployee() {
    this.employeeService.getEmployees().subscribe({
      next: (res) => {
        this.employees = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  deleteEmployee(id: number) {
    this.dialogService.onConfirmDialog("Are you sure you want to delete?").afterClosed()
      .subscribe(confirm => {
        if (confirm) {
          this.employeeService.deleteEmployee(id).subscribe({
            next: () => {
              this.errorMessage = '';
              this.getAllEmployee();
            },
            error: (err) => {
              this.errorMessage = err.error.message;
              console.log(err);
            },
          });
        }
      })
  }

  addEmployee(employee: any) {
    employee.departments = this.buildDepartmentJson(employee);
    this.employeeService.addEmployee(employee).subscribe({
      next: () => {
        this.errorMessage = '';
        this.getAllEmployee();
      },
      error: (err) => {
        this.errorMessage = err.error.message;
        console.log(err);
      },
    });
  }

  updateEmployee(employee: any) {
    employee.departments = this.buildDepartmentJson(employee);
    this.employeeService.updateEmployee(employee).subscribe({
      next: () => {
        this.errorMessage = '';
        this.getAllEmployee();
      },
      error: (err) => {
        this.errorMessage = err.error.message;
        console.log(err);
      },
    });
  }

  getAllDepartments() {
    this.departmentService.getDepartments().subscribe({
      next: (res) => {
        this.departments = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getCommaSeperatedNames(departments: any) {
    let name: string = '';
    for (let i = 0; i < departments.length; i++) {
      name = name.concat(departments [i].name).concat(i == departments.length - 1 ? "" : ", ");
    }
    return name;
  }

  buildDepartmentJson(employee: any) {
    let ids: string [] = [];
    employee.departments.forEach((ele: any) => {
      let result: any = {}
      result["id"] = ele;
      ids.push(result);
    })
    return ids;
  }

  closeAlert() {
    this.errorMessage = null;
  }
}
