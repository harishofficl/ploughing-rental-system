import { NgModule } from '@angular/core';

import { AuthRoutingModule } from './auth-routing.module';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';

@NgModule({
  declarations: [LoginComponent],
  imports: [AuthRoutingModule, FormsModule],
  exports: [],
})
export class AuthModule {}
