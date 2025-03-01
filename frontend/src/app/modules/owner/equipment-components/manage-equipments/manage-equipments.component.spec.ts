import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageEquipmentsComponent } from './manage-equipments.component';

describe('ManageEquipmentsComponent', () => {
  let component: ManageEquipmentsComponent;
  let fixture: ComponentFixture<ManageEquipmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageEquipmentsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageEquipmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
