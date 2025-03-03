import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../services/auth/auth.service';
import { ApiService } from '../../../../services/api/api.service';

@Component({
  selector: 'app-add-customer',
  templateUrl: './add-customer.component.html',
  styleUrls: ['./add-customer.component.css']
})

export class AddCustomerComponent {
  customerForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private auth: AuthService, private api: ApiService) {
    this.customerForm = this.formBuilder.group({
      name: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      email: ['', [Validators.required, Validators.email]],
      ownerId: [this.auth.currentUserId],
    });
  }

  onSubmit() {
    if (this.customerForm.valid) {
      const customerData = this.customerForm.getRawValue();
      this.api.postCustomer(customerData);

      // reset form
      this.customerForm.reset();
    }
  }
}