import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';
import { GpsService } from '../../../services/gps/gps.service';
import { ApiService } from '../../../services/api/api.service';

@Component({
  selector: 'app-home-driver',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  locationTrigger!: any;

  constructor(
    private auth: AuthService,
    private gps: GpsService,
    private api: ApiService
  ) {}

  ngOnInit() {
    this.locationTrigger = setInterval(() => {
      this.getLocation();
    }, 600000); // 10 minutes
  }

  async getLocation() {
    let gpsObject = {
      driverId: this.auth.currentUserId,
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
      };
      console.log(gpsObject);
      this.api.postGpsLocation(gpsObject);
    } catch (error) {
      throw error;
    }
  }

  ngOnDestroy() {
    clearInterval(this.locationTrigger);
  }
}
