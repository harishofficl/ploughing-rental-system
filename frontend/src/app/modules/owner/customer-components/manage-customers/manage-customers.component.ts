import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../../services/api/api.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { LoadingService } from '../../../../services/loading/loading.service';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-manage-customers',
  templateUrl: './manage-customers.component.html',
  styleUrls: ['./manage-customers.component.css']
})
export class ManageCustomersComponent implements OnInit {
  customers: any[] = [];
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
        this.fetchCustomers(this.auth.currentUserId, term);
      });

    this.fetchCustomers(this.auth.currentUserId);
  }

  fetchCustomers(ownerId: string, searchTerm: string = "", pageNumber: number = 0, size: number = 5): void {
    this.customers = [];
    this.loadingService.show();
    this.api.getCustomersByOwnerIdPaginated(ownerId, pageNumber, size, searchTerm).subscribe((response: any) => {
      this.page = response.data;
      this.customers = this.page.content;
      this.loadingService.hide();
    }), () => {
      this.loadingService.hide();
    };
  }

  searchCustomers() {
    this.searchSubject.next(this.searchTerm);
  }

  editCustomer(id: string) {
    //...
  }

  deleteCustomer(id: string) {
    //...
  }

  previousPage() {
    if (!this.page.first) {
      this.fetchCustomers(this.auth.currentUserId, this.searchTerm, this.page.number - 1);
    }
  }

  nextPage() {
    if (!this.page.last) {
      this.fetchCustomers(this.auth.currentUserId, this.searchTerm, this.page.number + 1);
    }
  }

  ngOnDestroy(): void {
    this.searchSubscription?.unsubscribe();
  }
}