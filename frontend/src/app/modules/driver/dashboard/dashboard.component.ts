import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';
import { GpsService } from '../../../services/gps/gps.service';

@Component({
  selector: 'app-dashboard-driver',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  userName: string;
  userLetters: string;
  constructor(public auth: AuthService, private gps: GpsService) {
    this.userName = this.auth.currentUserName;
    this.userLetters = this.userName
      .split(' ')
      .map((n) => n[0])
      .join('');
  }

  ngOnInit() {
    setInterval(() => {
      this.getLocation();
    }, 600000); // 10 minutes
  }

  async getLocation() {



    try {
      const position = await this.gps.getCurrentPosition();
      console.log(position.coords.latitude);
      console.log(position.coords.longitude);
      const istDate = new Date(position.timestamp).toLocaleString('en-IN', {
        timeZone: 'Asia/Kolkata',
      });
      console.log(istDate);
      return {position, istDate};
    } catch (error) {
      console.error('error', error);
      throw error;
    }
  }
}
