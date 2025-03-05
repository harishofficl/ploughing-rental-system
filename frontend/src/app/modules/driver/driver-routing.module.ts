import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AddFuelComponent } from '../../utils/add-fuel/add-fuel.component';
import { JobComponent } from './job/job.component';

const routes: Routes = [
  {
      path: '',
      component: HomeComponent,
      children: [
        {
          path: '',
          component: DashboardComponent,
        },
        {
          path: 'add-fuel',
          component: AddFuelComponent
        },
        {
          path: 'job',
          component: JobComponent,
        }
      ],
    },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule { }
