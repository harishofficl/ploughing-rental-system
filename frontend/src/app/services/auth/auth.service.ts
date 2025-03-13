import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  admin = {
    id: '67bd4a6b4eb4a03303ce1624',
    name: 'Super Admin',
    role: 'admin',
  };
  owner = {
    id: '67bd4a974eb4a03303ce1625',
    name: 'Seenivasan R',
    role: 'owner',
  };
  driver = { id: '67bd53c04874837ebcce0a36', name: 'Rajesh', role: 'driver' };

  currentUser;
  authToken: string;
  isAuthenticated: boolean = false;

  constructor(private router: Router) {
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

  adminLogin() {
    localStorage.setItem('currentUser', JSON.stringify(this.admin));
    localStorage.setItem(
      'authToken',
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0MTg0Mjc4MywiZXhwIjoxNzQxOTI5MTgzfQ.PokBRWgzRp0GrgpTTdSsRT1xlFXTxpeTELri95dk9_8'
    );
    this.currentUser = this.admin;
    const token = localStorage.getItem('authToken');
    if (token) {
      this.authToken = token;
    }
    this.isAuthenticated = true;
    this.router.navigate(['./admin']);
  }

  ownerLogin() {
    localStorage.setItem('currentUser', JSON.stringify(this.owner));
    localStorage.setItem(
      'authToken',
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWVuaXZhc2FuQGdtYWlsLmNvbSIsImlhdCI6MTc0MTg0MzI4MywiZXhwIjoxNzQxOTI5NjgzfQ.C9kxoKd0J047NGsmppmWbFttVyyj3Sf-PPuTSFsJJM8'
    );
    this.currentUser = this.owner;
    const token = localStorage.getItem('authToken');
    if (token) {
      this.authToken = token;
    }
    this.isAuthenticated = true;
    this.router.navigate(['./owner']);
  }

  driverLogin() {
    localStorage.setItem('currentUser', JSON.stringify(this.driver));
    localStorage.setItem(
      'authToken',
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWplc2hAZ21haWwuY29tIiwiaWF0IjoxNzQxODU2MDEzLCJleHAiOjE3NDE5NDI0MTN9.JbU8TSiJcafvD4c739Zi9RKi2B9KMy5RETI-ArIk7fM'
    );
    this.currentUser = this.driver;
    const token = localStorage.getItem('authToken');
    if (token) {
      this.authToken = token;
    }
    this.isAuthenticated = true;
    this.router.navigate(['./driver']);
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
