import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-dashboard-driver',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  userName: string;
  userLetters: string;
  constructor(public auth: AuthService) {
    this.userName = this.auth.currentUserName;
    this.userLetters = this.userName
      .split(' ')
      .map((n) => n[0])
      .join('');
  }
}
