import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GpsService {

  constructor() { }

  getCurrentPosition(): Promise<GeolocationPosition> {
    const options = {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 60000,
    };

    return new Promise((resolve, reject) => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(resolve, reject, options);
      } else {
        reject(new Error('Geolocation is not supported by this browser.'));
      }
    });
  }
}