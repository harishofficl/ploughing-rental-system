import { TestBed } from '@angular/core/testing';

import { DistancePricingService } from './distance-pricing.service';

describe('DistancePricingService', () => {
  let service: DistancePricingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DistancePricingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
