import { NgModule } from '@angular/core';
import {
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router,
  RouterModule,
  Routes,
} from '@angular/router';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CreateDriverComponent } from './driver-components/create-driver/create-driver.component';
import { ManageDriversComponent } from './driver-components/manage-drivers/manage-drivers.component';
import { CreateRentalRecordsComponent } from './rental-components/create-rental-records/create-rental-records.component';
import { ManageRentalRecordsComponent } from './rental-components/manage-rental-records/manage-rental-records.component';
import { AddEquipmentComponent } from './equipment-components/add-equipment/add-equipment.component';
import { ManageEquipmentsComponent } from './equipment-components/manage-equipments/manage-equipments.component';
import { LoadingService } from '../../services/loading/loading.service';
import { CreateVehicleComponent } from '../owner/vehicle-components/create-vehicle/create-vehicle.component';
import { ManageVehiclesComponent } from '../owner/vehicle-components/manage-vehicles/manage-vehicles.component';
import { BillCustomerComponent } from './bill-customer/bill-customer.component';

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
        path: 'create-driver',
        component: CreateDriverComponent,
      },
      {
        path: 'manage-drivers',
        component: ManageDriversComponent,
      },
      {
        path: 'create-rental-record',
        component: CreateRentalRecordsComponent,
      },
      {
        path: 'manage-rental-records',
        component: ManageRentalRecordsComponent,
      },
      {
        path: 'add-equipment',
        component: AddEquipmentComponent,
      },
      {
        path: 'manage-equipments',
        component: ManageEquipmentsComponent,
      },
      {
        path: 'add-vehicle',
        component: CreateVehicleComponent,
      },
      {
        path: 'manage-vehicles',
        component: ManageVehiclesComponent,
      },
      {
        path: 'bill-customer',
        component: BillCustomerComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OwnerRoutingModule {
  constructor(private router: Router, private loadingService: LoadingService) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        this.loadingService.show();
      } else if (
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel ||
        event instanceof NavigationError
      ) {
        this.loadingService.hide();
      }
    });
  }
}
