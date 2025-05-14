import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../../services/api/api.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { LoadingService } from '../../../../services/loading/loading.service';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-manage-equipments',
  templateUrl: './manage-equipments.component.html',
  styleUrls: ['./manage-equipments.component.css']
})
export class ManageEquipmentsComponent implements OnInit {
  equipments: any[] = [];
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
        this.fetchEquipments(this.auth.currentUserId, term);
      });

    this.fetchEquipments(this.auth.currentUserId);
  }

  fetchEquipments(ownerId: string, searchTerm: string = "", pageNumber: number = 0, size: number = 5): void {
    this.equipments = [];
    this.loadingService.show();
    this.api.getEquipmentsByOwnerIdPaginated(ownerId, pageNumber, size, searchTerm).subscribe((response: any) => {
      this.page = response.data;
      this.equipments = this.page.content;
      this.loadingService.hide();
    }), () => {
      this.loadingService.hide();
    };
  }

  searchEquipments() {
    this.searchSubject.next(this.searchTerm);
  }

  editEquipment(id: string) {
    
  }

  deleteEquipment(id: string) {
    // Logic to delete an equipment
  }

  previousPage() {
    if (!this.page.first) {
      this.fetchEquipments(this.auth.currentUserId, this.searchTerm, this.page.number - 1);
    }
  }

  nextPage() {
    if (!this.page.last) {
      this.fetchEquipments(this.auth.currentUserId, this.searchTerm, this.page.number + 1);
    }
  }

  ngOnDestroy(): void {
    this.searchSubscription?.unsubscribe();
  }
}