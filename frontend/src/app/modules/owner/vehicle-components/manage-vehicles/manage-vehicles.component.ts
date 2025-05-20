import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../../services/api/api.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { LoadingService } from '../../../../services/loading/loading.service';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-manage-vehicles',
  templateUrl: './manage-vehicles.component.html',
  styleUrls: ['./manage-vehicles.component.css']
})
export class ManageVehiclesComponent implements OnInit {
  vehicles: any[] = [];
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

  searchSubscription!: Subscription;
  searchSubject: Subject<string> = new Subject<string>();

  constructor(private api: ApiService, private auth: AuthService, public loadingService: LoadingService) {}

  ngOnInit(): void {
    this.searchSubscription = this.searchSubject
      .pipe(debounceTime(500))
      .subscribe((term: string) => {
        this.fetchVehicles(this.auth.currentUserId, term);
      });

    this.fetchVehicles(this.auth.currentUserId);
  }

  fetchVehicles(ownerId: string, searchTerm: string = "", pageNumber: number = 0, size: number = 5): void {
    this.vehicles = [];
    this.loadingService.show();
    this.api.getVehiclesByOwnerIdPaginated(ownerId, pageNumber, size, searchTerm).subscribe((response: any) => {
      this.page = response.data;
      this.vehicles = this.page.content;
      this.loadingService.hide();
    }), () => {
      this.loadingService.hide();
    };
  }

  searchVehicles() {
    this.searchSubject.next(this.searchTerm);
  }

  editVehicle(id: string) {
    // Logic to edit a vehicle
  }

  deleteVehicle(id: string) {
    // Logic to delete a vehicle
  }

  previousPage() {
    if (!this.page.first) {
      this.fetchVehicles(this.auth.currentUserId, this.searchTerm, this.page.number - 1);
    }
  }

  nextPage() {
    if (!this.page.last) {
      this.fetchVehicles(this.auth.currentUserId, this.searchTerm, this.page.number + 1);
    }
  }

  ngOnDestroy(): void {
    this.searchSubscription?.unsubscribe();
  }
}