import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  constructor(private http:HttpClient) { }
  baseUrl:string = "http://localhost:8080/department";

  getDepartments(){
    return this.http.get(this.baseUrl);
  }
  addDepartment(department:any){
    return this.http.post(this.baseUrl, department);
  }
  updateDepartment(department:any){
    return this.http.put(this.baseUrl, department);
  }
  deleteDepartment(id:number){
    return this.http.delete(this.baseUrl+"/"+id);
  }
}
