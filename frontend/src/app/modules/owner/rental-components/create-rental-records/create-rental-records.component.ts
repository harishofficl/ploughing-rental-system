import { Component, HostListener, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../services/auth/auth.service';
import { ApiService } from '../../../../services/api/api.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Subject } from 'rxjs';

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
  customers: any[] = [];
  private searchTerms = new Subject<string>();

  constructor(
    private formBuilder: FormBuilder,
    private auth: AuthService,
    private api: ApiService
  ) {
    this.ownerId = this.auth.currentUserId;
    const today = new Date().toISOString().split('T')[0];
    this.rentalForm = this.formBuilder.group({
      customerId: ['', Validators.required],
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

    this.searchTerms.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      switchMap(term => this.api.searchCustomersByOwnerId(this.ownerId, term))
    ).subscribe((customers) => {
      this.customers = customers.data;
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

  searchCustomers(event: Event): void {
    const term = (event.target as HTMLInputElement).value;
    this.searchTerms.next(term);
  }

  selectCustomer(customer: any): void {
    this.rentalForm.patchValue({ customerId: customer.id });
    (document.getElementById('customer') as HTMLInputElement).value = customer.name;
    this.customers = [];
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    const target = event.target as HTMLElement;
    if (!target.closest('.search-customer-input') && !target.closest('.suggestions')) {
      if (!this.rentalForm.get('customerId')?.value) {
        this.rentalForm.patchValue({ customerId: '' });
        (document.getElementById('customer') as HTMLInputElement).value = '';
      }
      this.customers = [];
    }
  }
}