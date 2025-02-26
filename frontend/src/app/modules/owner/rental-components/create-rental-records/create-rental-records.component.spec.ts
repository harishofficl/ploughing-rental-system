import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateRentalRecordsComponent } from './create-rental-records.component';

describe('CreateRentalRecordsComponent', () => {
  let component: CreateRentalRecordsComponent;
  let fixture: ComponentFixture<CreateRentalRecordsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateRentalRecordsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateRentalRecordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
