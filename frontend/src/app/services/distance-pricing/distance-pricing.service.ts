import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class DistancePricingService {

  private pricingRules: any[] = [];

  constructor(private api: ApiService, private authService: AuthService) {
    this.api
      .getPricingRules(this.authService.currentUserId)
      .subscribe((rules) => {
        this.pricingRules = rules.data;
      });
  }

  calculateTotalDistanceCost(hours: number, distance: number): number {
    let totalCost = 0;
    let maxHourDiscount = 0;
    for (const rule of this.pricingRules) {
      if (hours >= rule.hours) {
        maxHourDiscount = Math.max(maxHourDiscount, rule.hours);
        if (rule.hours >= maxHourDiscount) {
          const extraDistance = Math.max(0, distance - rule.freeDistance);
          totalCost = extraDistance * rule.pricePerKm;
        }
      }
    }
    return totalCost;
  }
}
