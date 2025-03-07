import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../../services/api/api.service';
import { AuthService } from '../../../../services/auth/auth.service';

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

  constructor(private api: ApiService, private auth: AuthService) {}

  ngOnInit(): void {
    this.fetchCustomers(this.auth.currentUserId);
  }

  fetchCustomers(ownerId: string, searchTerm: string = "", pageNumber: number = 0, size: number = 5): void {
    this.api.getCustomersByOwnerIdPaginated(ownerId, pageNumber, size, searchTerm).subscribe((response: any) => {
      this.page = response.data;
      this.customers = this.page.content;
    });
  }

  searchCustomers() {
    this.fetchCustomers(this.auth.currentUserId, this.searchTerm);
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
}