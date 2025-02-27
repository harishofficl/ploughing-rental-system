import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../services/auth/auth.service';
import { ApiService } from '../../../../services/api/api.service';

@Component({
  selector: 'app-add-equipment',
  templateUrl: './add-equipment.component.html',
  styleUrl: './add-equipment.component.css'
})
export class AddEquipmentComponent {
  equipmentForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private auth: AuthService, private api: ApiService) {
    this.equipmentForm = this.formBuilder.group({
      name: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]],
      ownerId: [this.auth.currentUserId],
    });
  }

  onSubmit() {
    if (this.equipmentForm.valid) {
      const equipmentData = this.equipmentForm.value;
      this.api.postEquipment(equipmentData);
    }
  }
}