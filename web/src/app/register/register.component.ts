import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name:string = '';
  email:string = '';
  password:string = '';

  constructor(private httpClient:HttpClient) {
  }

  register() {
    let body = {
      "name": this.name,
      "email": this.email,
      "password": this.password
    };
    this.httpClient.post("http://localhost:8080/user/register", body).subscribe( {
      next:(res: object) => {
        console.log(res);
        alert("Registration successful");
      },
      error:(msg:object) => {
        alert(msg);
      }
    });
  }
}
