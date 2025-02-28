import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';
import { GpsService } from '../../../services/gps/gps.service';
import { ApiService } from '../../../services/api/api.service';

@Component({
  selector: 'app-dashboard-driver',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  userName: string;
  userLetters: string;
  constructor(public auth: AuthService, private gps: GpsService, private api:ApiService) {
    this.userName = this.auth.currentUserName;
    this.userLetters = this.userName
      .split(' ')
      .map((n) => n[0])
      .join('');
  }

  ngOnInit() {
    // this.getLocation();

    setInterval(() => {
      this.getLocation();
    }, 600000);
  }

  async getLocation() {
    let gpsObject = {
      driverId: '',
      latitude: 0.0,
      longitude: 0.0,
      timeStamp: '',
    };

    try {
      const position = await this.gps.getCurrentPosition();
      const istDate = new Date(position.timestamp).toLocaleString('en-IN', {
        timeZone: 'Asia/Kolkata',
      });
      gpsObject = {
        driverId: this.auth.currentUserId,
        latitude: position.coords.latitude,
        longitude: position.coords.longitude,
        timeStamp: istDate,
      }
      console.log(gpsObject);
      this.api.postGpsLocation(gpsObject);
    } catch (error) {
      console.error('error', error);
      throw error;
    }
  }
}
