import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../../services/api/api.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { LoadingService } from '../../../../services/loading/loading.service';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-manage-rental-records',
  templateUrl: './manage-rental-records.component.html',
  styleUrls: ['./manage-rental-records.component.css'],
})
export class ManageRentalRecordsComponent implements OnInit {
  rentalRecords: any[] = [];
  searchTerm: string = '';
  page: any = {
    content: [],
    totalElements: 0,
    totalPages: 0,
    size: 10,
    number: 0,
    first: true,
    last: true,
  };

  searchSubscription!: Subscription;
  searchSubject: Subject<string> = new Subject<string>();

  constructor(
    private api: ApiService,
    private auth: AuthService,
    public loadingService: LoadingService
  ) {}

  ngOnInit(): void {
    this.searchSubscription = this.searchSubject
      .pipe(debounceTime(500))
      .subscribe((searchTerm) => {
        this.fetchRentalRecords(this.auth.currentUserId, searchTerm);
      });

    this.fetchRentalRecords(this.auth.currentUserId);
  }

  fetchRentalRecords(
    ownerId: string,
    searchTerm: string = '',
    pageNumber: number = 0,
    size: number = 5
  ): void {
    this.rentalRecords = [];
    this.loadingService.show();
    this.api
      .getRentalRecordsByOwnerIdPaginated(ownerId, pageNumber, size, searchTerm)
      .subscribe((response: any) => {
        this.page = response.data;
        this.rentalRecords = this.page.content;
        this.loadingService.hide();
      }),
      () => {
        this.loadingService.hide();
      };
  }

  searchRecords() {
    this.searchSubject.next(this.searchTerm);
  }

  editRecord(id: string) {
    // Logic to edit a record
  }

  deleteRecord(id: string) {
    // Logic to delete a record
  }

  previousPage() {
    if (!this.page.first) {
      this.fetchRentalRecords(
        this.auth.currentUserId,
        this.searchTerm,
        this.page.number - 1
      );
    }
  }

  nextPage() {
    if (!this.page.last) {
      this.fetchRentalRecords(
        this.auth.currentUserId,
        this.searchTerm,
        this.page.number + 1
      );
    }
  }

  ngOnDestroy(): void {
    this.searchSubscription?.unsubscribe();
  }
}
