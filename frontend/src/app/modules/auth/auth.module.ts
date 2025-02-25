import { NgModule } from '@angular/core';

import { AuthRoutingModule } from './auth-routing.module';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { NavBarComponent } from "../../nav-bar/nav-bar.component";

@NgModule({
  declarations: [LoginComponent],
  imports: [AuthRoutingModule, FormsModule, NavBarComponent],
  exports: [],
})
export class AuthModule {}
