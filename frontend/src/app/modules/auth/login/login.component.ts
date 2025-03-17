import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../../../services/api/api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm!: FormGroup;
  authToken!: string;

  constructor(
    private auth: AuthService,
    private fb: FormBuilder,
    private api: ApiService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  ngOnInit() {
    this.auth.logout();
  }

  login() {
    this.api.postLogin(this.loginForm.getRawValue()).subscribe((response) => {
      if (response?.accessToken) {
        this.authToken = response.accessToken;
        const email = this.loginForm.get('email')?.value;
        this.auth.login(this.authToken, email);
      } else {
        console.error('Invalid login response:', response);
      }
    });
  }
}
