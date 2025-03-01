import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { ApiService } from '../../services/api/api.service';

@Component({
  selector: 'app-add-fuel',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-fuel.component.html',
  styleUrl: './add-fuel.component.css'
})
export class AddFuelComponent {
  fuelForm: FormGroup;
  vehicles!: any;
  driver: any;

  constructor(private formBuilder: FormBuilder, private auth: AuthService, private api: ApiService) {
    this.fuelForm = this.formBuilder.group({
      vehicleId: ["", Validators.required],
      fuelAmount: ["", Validators.required],
    });
  }

  ngOnInit() {
    this.api.getDriverById(this.auth.currentUserId).subscribe((driver) => {
      this.driver = driver.data;

      this.api.getVehiclesByOwnerId(this.driver.ownerId).subscribe((vehicles) => {
        this.vehicles = vehicles.data;
      });
    });
  }

  onSubmit() {
    if (this.fuelForm.valid) {
      const fuelData = this.fuelForm.value;
      this.api.addFuel(fuelData);
      this.fuelForm.patchValue({ vehicleId: "", fuelAmount: "" });
    }
  }
}