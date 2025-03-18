import { Routes } from '@angular/router';
import { AuthModule } from './modules/auth/auth.module';
import { PaymentCallbackComponent } from './utils/payment-callback/payment-callback.component';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => AuthModule,
  },
  {
    path: 'admin',
    loadChildren: () =>
      import('./modules/admin/admin.module').then((m) => m.AdminModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'driver',
    loadChildren: () =>
      import('./modules/driver/driver.module').then((m) => m.DriverModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'owner',
    loadChildren: () =>
      import('./modules/owner/owner.module').then((m) => m.OwnerModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'payment/callback',
    component: PaymentCallbackComponent,
  },
];
