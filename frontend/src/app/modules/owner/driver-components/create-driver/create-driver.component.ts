import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../services/auth/auth.service';
import { ApiService } from '../../../../services/api/api.service';

@Component({
  selector: 'app-create-driver',
  templateUrl: './create-driver.component.html',
  styleUrl: './create-driver.component.css'
})
export class CreateDriverComponent {
  driverForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private auth: AuthService, private api: ApiService) {
    this.driverForm = this.formBuilder.group({
      name: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      ownerId: [this.auth.currentUserId],
    });
  }

  onSubmit() {
    if (this.driverForm.valid) {
      const driverData = this.driverForm.getRawValue();
      this.api.postDriver(driverData);
    }
  }
}
