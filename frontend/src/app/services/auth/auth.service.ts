import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  admin = { id: '67bd4a6b4eb4a03303ce1624', name: 'Super Admin', role: 'admin' };
  owner = { id: '67bd4a974eb4a03303ce1625', name: 'Seenivasan R', role: 'owner' };
  driver = { id: '67bd53c04874837ebcce0a36', name: 'Kumar', role: 'driver' };

  currentUser;
  isAuthenticated: boolean = false;

  constructor(private router: Router) {
    const user = localStorage.getItem('currentUser');
    if (user) {
      this.currentUser = JSON.parse(user);
      this.isAuthenticated = true;
    } else {
      this.currentUser = { id: '', name: '', role: '' };
    }
  }

  adminLogin() {
    localStorage.setItem('currentUser', JSON.stringify(this.admin));
    this.currentUser = this.admin;
    this.isAuthenticated = true;
    this.router.navigate(['./admin']);
  }

  ownerLogin() {
    localStorage.setItem('currentUser', JSON.stringify(this.owner));
    this.currentUser = this.owner;
    this.isAuthenticated = true;
    this.router.navigate(['./owner']);
  }

  driverLogin() {
    localStorage.setItem('currentUser', JSON.stringify(this.driver));
    this.currentUser = this.driver;
    this.isAuthenticated = true;
    this.router.navigate(['./driver']);
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.currentUser = { id: '', name: '', role: '' };
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
}
