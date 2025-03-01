import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators, FormArray } from '@angular/forms';
import { ApiService } from '../../../services/api/api.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { AuthService } from '../../../services/auth/auth.service';
import { PaymentService } from '../../../services/payment/payment.service';

@Component({
  selector: 'app-bill-customer',
  templateUrl: './bill-customer.component.html',
  styleUrls: ['./bill-customer.component.css'],
})
export class BillCustomerComponent implements OnInit {
  billForm: FormGroup;
  ownerId!: string;
  customers: any[] = [];
  selectedCustomer!: any;
  unpaidRecords: any[] = [];
  noCustomerDisplay: boolean = false;
  changeCustomerDisplay: boolean = false;
  public searchTerms = new Subject<string>();

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private authService: AuthService,
    private paymentService: PaymentService
  ) {
    this.ownerId = this.authService.currentUserId;
    this.billForm = this.fb.group({
      customerId: ['', Validators.required],
      ownerId: [this.authService.currentUserId],
      totalAmount: [{ value: '', disabled: true }],
      paid: [false, Validators.required],
      rentalRecordIds: this.fb.array([]),
    });
  }

  ngOnInit() {
    this.searchTerms
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        switchMap((term) =>
          this.apiService.searchCustomersByOwnerId(this.ownerId, term)
        )
      )
      .subscribe((customers) => {
        this.customers = customers.data;
        this.noCustomerDisplay = customers.data.length === 0;
      });
  }

  searchCustomers(event: Event): void {
    const term = (event.target as HTMLInputElement).value;
    this.searchTerms.next(term);
  }

  selectCustomer(customer: any): void {
    this.selectedCustomer = customer;
    this.billForm.patchValue({ customerId: customer.id });
    const customerInput = document.getElementById(
      'customer'
    ) as HTMLInputElement;
    customerInput.value = customer.name;
    customerInput.disabled = true;
    this.changeCustomerDisplay = true;
    this.customers = [];
    this.calculateTotalAmount(customer.id);
  }

  changeCustomer(): void {
    this.billForm.patchValue({ customerId: '' });
    const customerInput = document.getElementById(
      'customer'
    ) as HTMLInputElement;
    customerInput.disabled = false;
    customerInput.focus();
    this.changeCustomerDisplay = false;
  }

  calculateTotalAmount(customerId: string): void {
    this.apiService
      .getUnpaidRecordsByCustomerId(customerId)
      .subscribe((records) => {
        this.unpaidRecords = records.data;
        if (this.unpaidRecords.length > 0) {
          const totalAmount = this.unpaidRecords.reduce(
            (total: number, record: any) => total + record.totalCost,
            0
          );
          const rentalRecordIds = this.billForm.get('rentalRecordIds') as FormArray;
          rentalRecordIds.clear();
          this.unpaidRecords.forEach((record) => {
            rentalRecordIds.push(new FormControl(record.id));
          });
          this.billForm.patchValue({ totalAmount });
        } else {
          this.billForm.reset();
          this.changeCustomerDisplay = false;
          const customerInput = document.getElementById(
            'customer'
          ) as HTMLInputElement;
          customerInput.disabled = false;
          this.billForm.patchValue({ paid: false });
        }
      });
  }

  onSubmit() {
    if (this.billForm.valid) {
      const billData = this.billForm.getRawValue();
      // this.apiService.postBill(billData, this.selectedCustomer);

      this.paymentService.createPaymentLink(this.selectedCustomer.email, billData.totalAmount).subscribe(() => {
        console.log('Payment link created ✔️');
      });

      // Reset form
      this.changeCustomerDisplay = false;
      const customerInput = document.getElementById(
        'customer'
      ) as HTMLInputElement;
      customerInput.disabled = false;
      this.billForm.patchValue({ customerId: '' });
      this.billForm.patchValue({ totalAmount: '' });
      this.billForm.patchValue({ ownerId: this.authService.currentUserId });
      this.billForm.patchValue({ paid: false });
    }
  }
}