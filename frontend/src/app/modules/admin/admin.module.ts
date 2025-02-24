import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';

@NgModule({
  declarations: [HomeComponent, DashboardComponent],
  imports: [CommonModule, FormsModule, AdminRoutingModule, ReactiveFormsModule],
})
export class AdminModule {}
