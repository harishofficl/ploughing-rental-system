import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    if (this.isLoggedIn()) {
      return true;
    } else {
      Swal.fire({
        icon: 'warning',
        title: 'Access Denied',
        text: 'Please log in to access this page',
        confirmButtonText: 'OK',
      });
      this.router.navigate(['']);
      return false;
    }
  }

  private isLoggedIn(): boolean {
    if (localStorage.getItem('authToken') != null) {
      return true;
    }
    return false;
  }
}
