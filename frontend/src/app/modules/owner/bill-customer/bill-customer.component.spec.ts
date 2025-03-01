import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillCustomerComponent } from './bill-customer.component';

describe('BillCustomerComponent', () => {
  let component: BillCustomerComponent;
  let fixture: ComponentFixture<BillCustomerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BillCustomerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BillCustomerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
