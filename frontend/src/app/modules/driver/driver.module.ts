import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriverRoutingModule } from './driver-routing.module';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SidebarComponent } from './sidebar/sidebar.component';
import { NavBarComponent } from '../../utils/nav-bar/nav-bar.component';
import { JobComponent } from './job/job.component';
import { MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { WebcamModule } from 'ngx-webcam';
import { ListJobsComponent } from './list-jobs/list-jobs.component';

@NgModule({
  declarations: [
    HomeComponent,
    DashboardComponent,
    SidebarComponent,
    JobComponent,
    ListJobsComponent,
  ],
  imports: [
    CommonModule,
    DriverRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NavBarComponent,
    MatStepperModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    WebcamModule,
  ],
  exports: [HomeComponent, ListJobsComponent],
})
export class DriverModule {}
