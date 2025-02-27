import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../services/auth/auth.service';
import { ApiService } from '../../../../services/api/api.service';

@Component({
  selector: 'app-create-rental-records',
  templateUrl: './create-rental-records.component.html',
  styleUrls: ['./create-rental-records.component.css'],
})
export class CreateRentalRecordsComponent implements OnInit {
  rentalForm: FormGroup;
  ownerId!: string;
  drivers: any[] = [];
  equipment: any[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private auth: AuthService,
    private api: ApiService
  ) {
    this.ownerId = this.auth.currentUserId;
    const today = new Date().toISOString().split('T')[0];
    this.rentalForm = this.formBuilder.group({
      ownerId: [this.auth.currentUserId],
      driverId: ['', Validators.required],
      equipment: ['', Validators.required],
      date: [today, Validators.required],
      hoursUsed: ['', [Validators.required, Validators.min(0)]],
      ratePerHour: [{ value: '', disabled: true }],
      totalCost: [{ value: '', disabled: true }],
      isPaid: [false, Validators.required],
    });
  }

  ngOnInit() {
    this.api.getDriversByOwnerId(this.ownerId).subscribe((drivers) => {
      this.drivers = drivers.data;
    });
    this.api.getEquipmentsByOwnerId(this.ownerId).subscribe((equipment) => {
      this.equipment = equipment.data;
    });
  }

  onEquipmentChange(event: Event) {
    const equipmentId = (event.target as HTMLSelectElement).value;
    this.api.getEquipmentById(equipmentId).subscribe((equipment) => {
      this.rentalForm.patchValue({ ratePerHour: equipment.data.price });
      this.calculateTotalCost();
    });
  }

  calculateTotalCost() {
    const hoursUsed = this.rentalForm.get('hoursUsed')?.value;
    const ratePerHour = this.rentalForm.get('ratePerHour')?.value;
    if (hoursUsed && ratePerHour) {
      const totalCost = hoursUsed * ratePerHour;
      this.rentalForm.patchValue({ totalCost });
    }
  }

  onSubmit() {
    if (this.rentalForm.valid) {
      const rentalData = this.rentalForm.getRawValue();
      this.api.postRentalRecord(rentalData);
    }
  }
}
