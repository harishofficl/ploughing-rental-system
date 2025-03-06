import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NavBarComponent } from "../../utils/nav-bar/nav-bar.component";
import { SidebarComponent } from './sidebar/sidebar.component';
import { StatsComponent } from './stats/stats.component';

@NgModule({
  declarations: [HomeComponent, DashboardComponent, SidebarComponent, StatsComponent],
  imports: [CommonModule, FormsModule, AdminRoutingModule, ReactiveFormsModule, NavBarComponent],
})
export class AdminModule {}
