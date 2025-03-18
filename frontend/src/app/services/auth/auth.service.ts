import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  currentUser;
  authToken: string;
  isAuthenticated: boolean = false;

  constructor(private router: Router, private api: ApiService) {
    const user = localStorage.getItem('currentUser');
    const token = localStorage.getItem('authToken');
    if (user && token) {
      this.currentUser = JSON.parse(user);
      this.isAuthenticated = true;
      this.authToken = token;
    } else {
      this.currentUser = { id: '', name: '', role: '' };
      this.authToken = '';
    }
  }

  login(authToken: string, email: string) {
    this.authToken = authToken;
    if (this.authToken) {
      localStorage.setItem('authToken', this.authToken);
      this.api.getUserByEmail(email).subscribe((response) => {
        this.currentUser = response;
        localStorage.setItem('currentUser', JSON.stringify(this.currentUser));
        const route = this.currentUser.role;
        this.router.navigate([`./${route}`]);
        this.isAuthenticated = true;
      });
    }
  }

  logout() {
    localStorage.clear();
    this.currentUser = { id: '', name: '', role: '' };
    this.authToken = '';
    this.isAuthenticated = false;
    this.router.navigate(['./']);
  }

  get currentUserId() {
    return this.currentUser.id;
  }

  get currentUserName() {
    return this.currentUser.name;
  }

  get currentUserRole() {
    return this.currentUser.role;
  }

  get getToken() {
    return this.authToken;
  }
}
