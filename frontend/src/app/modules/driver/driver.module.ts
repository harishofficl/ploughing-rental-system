import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriverRoutingModule } from './driver-routing.module';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SidebarComponent } from './sidebar/sidebar.component';


@NgModule({
  declarations: [HomeComponent, DashboardComponent, SidebarComponent],
  imports: [
    CommonModule,
    DriverRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class DriverModule { }
