import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { catchError } from 'rxjs';
import Swal from 'sweetalert2';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken;

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(req).pipe(
    catchError((error) => {
      if (error.error.errorCode === 'JWT_EXPIRED') {
        Swal.fire('Error', 'Session expired. Please login again.', 'error');
        authService.logout();
      } else {
        console.error(error);
        Swal.fire('Error', error.error.message, 'error');
      }
      throw error.error;
    })
  );
};
