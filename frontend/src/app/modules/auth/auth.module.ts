import { NgModule } from '@angular/core';

import { AuthRoutingModule } from './auth-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { NavBarComponent } from "../../utils/nav-bar/nav-bar.component";
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [LoginComponent],
  imports: [AuthRoutingModule, FormsModule, NavBarComponent, CommonModule, ReactiveFormsModule],
  exports: [],
})
export class AuthModule {}
