import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar-driver',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent {
  isVehicleOpen: boolean = false;


  toggleVehicle() {
    this.isVehicleOpen = !this.isVehicleOpen;
  }

}
