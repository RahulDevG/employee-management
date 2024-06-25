import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private http:HttpClient) { }
  baseUrl:string = "http://localhost:8080/employee";

  getEmployees(){
    return this.http.get(this.baseUrl);
  }
  addEmployee(data:any){
    return this.http.post(this.baseUrl, data);
  }
  updateEmployee(data:any){
    return this.http.put(this.baseUrl, data);
  }
  deleteEmployee(id:number){
    return this.http.delete(this.baseUrl+"/"+id);
  }
}
