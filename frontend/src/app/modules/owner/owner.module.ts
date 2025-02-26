import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OwnerRoutingModule } from './owner-routing.module';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SidebarComponent } from './sidebar/sidebar.component';
import { NavBarComponent } from '../../utils/nav-bar/nav-bar.component';
import { CreateDriverComponent } from './driver-components/create-driver/create-driver.component';
import { ManageDriversComponent } from './driver-components/manage-drivers/manage-drivers.component';
import { AddEquipmentComponent } from './equipment-components/add-equipment/add-equipment.component';
import { ManageEquipmentsComponent } from './equipment-components/manage-equipments/manage-equipments.component';
import { CreateRentalRecordsComponent } from './rental-components/create-rental-records/create-rental-records.component';
import { ManageRentalRecordsComponent } from './rental-components/manage-rental-records/manage-rental-records.component';
import { LoadingComponent } from '../../utils/loading/loading.component';

@NgModule({
  declarations: [
    HomeComponent,
    DashboardComponent,
    SidebarComponent,
    CreateDriverComponent,
    ManageDriversComponent,
    AddEquipmentComponent,
    ManageEquipmentsComponent,
    CreateRentalRecordsComponent,
    ManageRentalRecordsComponent,
  ],
  imports: [
    CommonModule,
    OwnerRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NavBarComponent,
    LoadingComponent,
  ],
})
export class OwnerModule {}
