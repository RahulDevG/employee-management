import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email:string = '';
  password:string = '';

  constructor(private router:Router, private httpClient:HttpClient) {
  }

  login() {
    let body = {
      "email": this.email,
      "password":this.password
    }
    this.httpClient.post("http://localhost:8080/user/login", body).subscribe( {
      next: () => {
        this.router.navigateByUrl('/home');
      },
      error: (err) => {
        alert(err.error.message);
      }
    });
  }
}
