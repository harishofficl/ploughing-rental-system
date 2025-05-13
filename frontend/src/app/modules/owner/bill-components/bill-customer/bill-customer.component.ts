import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../../../../services/api/api.service';
import { debounceTime, switchMap } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { AuthService } from '../../../../services/auth/auth.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
  selector: 'app-bill-customer',
  templateUrl: './bill-customer.component.html',
  styleUrls: ['./bill-customer.component.css'],
})
export class BillCustomerComponent implements OnInit, AfterViewInit {
  billForm: FormGroup;
  ownerId!: string;
  customers: any[] = [];
  selectedCustomer!: any;
  unpaidRecords: any[] = [];
  noCustomerDisplay: boolean = false;
  changeCustomerDisplay: boolean = false;
  public searchTerms = new Subject<string>();

  displayedColumns: string[] = [
    'select',
    'equipment',
    'date',
    'hoursUsed',
    'totalCost',
  ];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<any>([]);
  selection = new SelectionModel<any>(true, []);

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private authService: AuthService
  ) {
    this.ownerId = this.authService.currentUserId;
    this.billForm = this.fb.group({
      customerId: ['', Validators.required],
      customerName: [''],
      ownerId: [this.authService.currentUserId],
      totalAmount: [{ value: '', disabled: true }],
      paid: [false, Validators.required],
      allMethods: [false],
      rentalRecordIds: this.fb.array([]),
    });
  }

  ngOnInit() {
    this.searchTerms
      .pipe(
        debounceTime(500),
        switchMap((term) =>
          this.apiService.searchCustomersByOwnerId(this.ownerId, term)
        )
      )
      .subscribe((customers) => {
        this.customers = customers.data;
        this.noCustomerDisplay = customers.data.length === 0;
      });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  searchCustomers(event: Event): void {
    const term = (event.target as HTMLInputElement).value;
    this.searchTerms.next(term);
  }

  selectCustomer(customer: any): void {
    this.selectedCustomer = customer;
    this.billForm.patchValue({ customerId: customer.id });
    this.billForm.patchValue({ customerName: customer.name });
    const customerInput = document.getElementById(
      'customer'
    ) as HTMLInputElement;
    customerInput.value = customer.name;
    customerInput.disabled = true;
    this.changeCustomerDisplay = true;
    this.customers = [];

    this.fetchUnpaidRecords(customer.id);
  }

  changeCustomer(): void {
    this.billForm.patchValue({ customerId: '' });
    this.billForm.patchValue({ totalAmount: '' });
    const customerInput = document.getElementById(
      'customer'
    ) as HTMLInputElement;
    customerInput.disabled = false;
    customerInput.focus();
    this.changeCustomerDisplay = false;

    this.unpaidRecords = [];
    this.dataSource.data = [];
    this.selection.clear();
  }

  fetchUnpaidRecords(customerId: string): void {
    this.apiService
      .getUnpaidRecordsByCustomerId(customerId)
      .subscribe((records) => {
        if ( records.success === false ){
          this.changeCustomer();
          return;
        } 
        this.unpaidRecords = records.data;
        this.dataSource = new MatTableDataSource(this.unpaidRecords);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

        this.selection.clear();
        this.updateTotalAmount();
      });
  }

  // applyFilter(event: Event) {
  //   const filterValue = (event.target as HTMLInputElement).value
  //     .trim()
  //     .toLowerCase();
  //   this.dataSource.filter = filterValue;
  // }

  toggleAllRows(event: any): void {
    if (event.checked) {
      this.selection.select(...this.dataSource.data);
    } else {
      this.selection.clear();
    }
    this.updateTotalAmount();
  }

  isAllSelected(): boolean {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  isSomeSelected(): boolean {
    return this.selection.selected.length > 0 && !this.isAllSelected();
  }

  updateTotalAmount(): void {
    let totalAmount = 0;
    this.selection.selected.forEach((row) => {
      totalAmount += row.totalCost;
    });
    this.billForm.patchValue({ totalAmount });
  }

  toggleRowSelection(row: any): void {
    this.selection.toggle(row);
    this.updateTotalAmount();
  }

  onSubmit() {
    if (this.billForm.valid) {
      const billData = this.billForm.getRawValue();
      const totalAmount = billData.totalAmount;

      if (totalAmount <= 0) {
        return; // validation error message pending.....
      }

      const selectedRentalRecordIds = this.unpaidRecords
        .filter((record) => this.selection.isSelected(record))
        .map((record) => record.id);

      billData.rentalRecordIds = selectedRentalRecordIds;

      this.apiService.postBill(billData, this.selectedCustomer, billData.paid);

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
      this.billForm.patchValue({ allMethods: false });

      this.unpaidRecords = [];
      this.dataSource.data = [];
      this.selection.clear();
    }
  }
}
