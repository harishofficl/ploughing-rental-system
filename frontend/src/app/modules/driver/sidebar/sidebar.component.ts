import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar-driver',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent {
  isDriverOpen: boolean = false;
  isEquipmentOpen: boolean = false;
  isRentalOpen: boolean = false;

  toggleDriver() {
    this.isDriverOpen = !this.isDriverOpen;
  }

  toggleEquipment() {
    this.isEquipmentOpen = !this.isEquipmentOpen;
  }

  toggleRental() {
    this.isRentalOpen = !this.isRentalOpen;
  }
}
