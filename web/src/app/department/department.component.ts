import {Component, OnInit} from '@angular/core';
import {DepartmentService} from "./department.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DialogService} from "../confirmation-dialog/dialog.service";

@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.css']
})
export class DepartmentComponent implements OnInit {
  errorMessage: string = null;
  departments: any;
  departmentForm!: FormGroup;

  constructor(private departmentService: DepartmentService,
              private dialogService: DialogService) {
  }

  ngOnInit() {
    this.getAllDepartments();
    this.resetForm();
  }

  resetForm() {
    this.errorMessage = null;
    this.departmentForm = new FormGroup({
      name: new FormControl('', Validators.required),
      mandatory: new FormControl(false),
      readOnly: new FormControl(false),

    });
  }

  populateFormData(department: any) {
    this.errorMessage = null;
    this.departmentForm = new FormGroup({
      id: new FormControl(department.id),
      name: new FormControl(department.name, Validators.required),
      mandatory: new FormControl(department.mandatory),
      readOnly: new FormControl(department.readOnly),

    });
  }

  getAllDepartments() {
    this.departmentService.getDepartments().subscribe({
      next: (res: object) => {
        this.departments = res;
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = err.error.message();
        console.log(err.error);
      }
    })
  }

  deleteDepartment(id: number) {
    this.dialogService.onConfirmDialog("Are you sure you want to delete?").afterClosed()
      .subscribe(confirm => {
        if (confirm) {
          this.departmentService.deleteDepartment(id).subscribe({
            next: () => {
              this.errorMessage = '';
              this.getAllDepartments();
            },
            error: (err: HttpErrorResponse) => {
              this.errorMessage = err.error.message;
              console.log(err.error);
            },
          });
        }
      })
  }

  addDepartment(department: object) {
    this.departmentService.addDepartment(department).subscribe({
      next: () => {
        this.errorMessage = '';
        this.getAllDepartments();
      },
      error: (err) => {
        this.errorMessage = err.error.message;
        console.log(err);
      },
    });
  }

  updateDepartment(department: object) {
    this.departmentService.updateDepartment(department).subscribe({
      next: () => {
        this.errorMessage = '';
        this.getAllDepartments();
      },
      error: (err) => {
        this.errorMessage = err.error.message;
        console.log(err);
      },
    });
  }

  closeAlert() {
    this.errorMessage = null;
  }
}
