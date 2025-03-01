import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';
import { ApiService } from '../../../services/api/api.service';

@Component({
  selector: 'app-dashboard-owner',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {

  totalOutstandingBillAmount!: number;
  totalCustomers!: number;
  userName: string;
  userLetters: string;
  
  constructor(public auth: AuthService, private api: ApiService) {
    this.userName = this.auth.currentUserName;
    this.userLetters = this.userName.split(' ').map((n) => n[0]).join('');

    this.api.getOutstandingBillAmount(this.auth.currentUserId).subscribe((response) => {
      this.totalOutstandingBillAmount = response.data;
    });

    this.api.getCustomersCount(this.auth.currentUserId).subscribe((response) => {
      this.totalCustomers = response.data;
    });
  }
}
