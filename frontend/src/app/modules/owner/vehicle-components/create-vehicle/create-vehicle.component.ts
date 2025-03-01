import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../services/auth/auth.service';
import { ApiService } from '../../../../services/api/api.service';

@Component({
  selector: 'app-create-vehicle',
  templateUrl: './create-vehicle.component.html',
  styleUrl: './create-vehicle.component.css'
})
export class CreateVehicleComponent {
  vehicleForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private auth: AuthService, private api: ApiService) {
    this.vehicleForm = this.formBuilder.group({
      name: ['', Validators.required],
      ownerId: [this.auth.currentUserId],
    });
  }

  onSubmit() {
    if (this.vehicleForm.valid) {
      const vehicleData = this.vehicleForm.value;
      this.api.postVehicle(vehicleData);
      this.vehicleForm.patchValue({ name: '' });
    }
  }
}