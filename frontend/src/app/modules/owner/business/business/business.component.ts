import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../../../../services/api/api.service';
import { AuthService } from '../../../../services/auth/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-business',
  templateUrl: './business.component.html',
  styleUrls: ['./business.component.css'],
})
export class BusinessComponent {
  pricingForm: FormGroup;
  pricingRules: any[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private api: ApiService,
    private authService: AuthService
  ) {
    this.pricingForm = this.formBuilder.group({
      hours: ['', [Validators.required, Validators.min(0)]],
      freeDistance: ['', [Validators.required, Validators.min(0)]],
      pricePerKm: ['', [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit() {
    this.updateRulesDisplay();
  }

  updateRulesDisplay() {
    this.api
      .getPricingRules(this.authService.currentUserId)
      .subscribe((rules) => {
        this.pricingRules = rules.data;
        this.pricingRules.sort((a, b) => a.hours - b.hours);
      });
  }

  deleteRule(index: number) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete the rule?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!',
    }).then((result) => {
      if (result.isConfirmed) {
        this.pricingRules.splice(index, 1);
        this.api
          .setPricingRules(this.pricingRules, this.authService.currentUserId)
          .subscribe(() => {
            Swal.fire(
              'Success',
              'Pricing rules updated successfully',
              'success'
            );
            this.updateRulesDisplay();
          });
      }
    });
  }

  onSubmit() {
    if (this.pricingForm.valid) {
      const { hours, freeDistance, pricePerKm } =
        this.pricingForm.getRawValue();
      this.pricingRules.push({ hours, freeDistance, pricePerKm });
      this.pricingForm.reset();
    }
    this.api
      .setPricingRules(this.pricingRules, this.authService.currentUserId)
      .subscribe(() => {
        Swal.fire('Success', 'Pricing rules updated successfully', 'success');
        this.updateRulesDisplay();
      });
  }
}
