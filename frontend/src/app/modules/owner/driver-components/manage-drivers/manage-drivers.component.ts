import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../../services/api/api.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { LoadingService } from '../../../../services/loading/loading.service';

@Component({
  selector: 'app-manage-drivers',
  templateUrl: './manage-drivers.component.html',
  styleUrls: ['./manage-drivers.component.css']
})
export class ManageDriversComponent implements OnInit {
  drivers: any[] = [];
  searchTerm: string = '';
  page: any = {
    content: [],
    totalElements: 0,
    totalPages: 0,
    size: 10,
    number: 0,
    first: true,
    last: true
  };

  constructor(private api: ApiService, private auth: AuthService, public loadingService:LoadingService) {}

  ngOnInit(): void {
    this.fetchDrivers(this.auth.currentUserId);
  }

  fetchDrivers(ownerId: string, searchTerm: string = "", pageNumber: number = 0, size: number = 5): void {
    this.drivers = [];
    this.loadingService.show();
    this.api.getDriversByOwnerIdPaginated(ownerId, pageNumber, size, searchTerm).subscribe((response: any) => {
      this.page = response.data;
      this.drivers = this.page.content;
      this.loadingService.hide();
    }), () => {
      this.loadingService.hide();
    };
  }

  searchDrivers() {
    this.fetchDrivers(this.auth.currentUserId, this.searchTerm);
  }

  editDriver(id: string) {
    // Logic to edit a driver
  }

  deleteDriver(id: string) {
    // Logic to delete a driver
  }

  previousPage() {
    if (!this.page.first) {
      this.fetchDrivers(this.auth.currentUserId, this.searchTerm, this.page.number - 1);
    }
  }

  nextPage() {
    if (!this.page.last) {
      this.fetchDrivers(this.auth.currentUserId, this.searchTerm, this.page.number + 1);
    }
  }
}