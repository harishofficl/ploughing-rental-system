import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageRentalRecordsComponent } from './manage-rental-records.component';

describe('ManageRentalRecordsComponent', () => {
  let component: ManageRentalRecordsComponent;
  let fixture: ComponentFixture<ManageRentalRecordsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageRentalRecordsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageRentalRecordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
