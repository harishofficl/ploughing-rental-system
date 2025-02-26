import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-create-driver',
  templateUrl: './create-driver.component.html',
  styleUrl: './create-driver.component.css'
})
export class CreateDriverComponent {
  driverForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.driverForm = this.formBuilder.group({
      name: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      ownerId: [''],
    });
  }

  ngOnInit() {}

  onSubmit() {
    if (this.driverForm.valid) {
      const driverData = this.driverForm.value;
      console.log('Driver Created:', driverData);
    }
  }
}
