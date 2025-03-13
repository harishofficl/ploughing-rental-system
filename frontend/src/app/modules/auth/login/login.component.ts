import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {

  constructor(private auth: AuthService) {}

  ngOnInit() {
    this.auth.logout();
  }

  adminLogin() {
    this.auth.adminLogin();
  }
  
  ownerLogin() {
    this.auth.ownerLogin();
  }

  driverLogin() {
    this.auth.driverLogin();
  }
}
