import { Routes } from '@angular/router';
import { AuthModule } from './modules/auth/auth.module';
import { PaymentCallbackComponent } from './utils/payment-callback/payment-callback.component';

export const routes: Routes = [
    {
        path: '',
        loadChildren: () => AuthModule,
      },
      {
        path: 'admin',
        loadChildren: () => import('./modules/admin/admin.module').then((m) => m.AdminModule),
      },
      {
        path: 'driver',
        loadChildren: () => import('./modules/driver/driver.module').then((m) => m.DriverModule),
      },
      {
        path: 'owner',
        loadChildren: () => import('./modules/owner/owner.module').then((m) => m.OwnerModule),
      },
      {
        path: 'payment/callback',
        component: PaymentCallbackComponent,
      }
];
