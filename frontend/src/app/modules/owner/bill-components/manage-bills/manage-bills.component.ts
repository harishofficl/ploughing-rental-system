import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../../services/api/api.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { LoadingService } from '../../../../services/loading/loading.service';
import Swal from 'sweetalert2';
import { PaymentService } from '../../../../services/payment/payment.service';

@Component({
  selector: 'app-manage-bills',
  templateUrl: './manage-bills.component.html',
  styleUrls: ['./manage-bills.component.css']
})
export class ManageBillsComponent implements OnInit {
  bills: any[] = [];
  searchTerm: string = '';
  page: any = {
    content: [],
    totalElements: 0,
    totalPages: 0,
    size: 10,
    number: 0,
    first: true,
    last: true
  };

  constructor(private api: ApiService, private auth: AuthService, public loadingService: LoadingService, private paymentService: PaymentService) {}

  ngOnInit(): void {
    this.fetchBills(this.auth.currentUserId);
  }

  fetchBills(ownerId: string, searchTerm: string = "", pageNumber: number = 0, size: number = 5): void {
    this.bills = [];
    this.loadingService.show();
    this.api.getBillsByOwnerIdPaginated(ownerId, pageNumber, size, searchTerm).subscribe((response: any) => {
      this.page = response.data;
      this.bills = this.page.content;
      this.loadingService.hide();
    }), () => {
      this.loadingService.hide();
    };
  }

  createPayment(index: number) {
    const bill = this.bills[index];
    if(bill.paid) {
      Swal.fire("Warning", "Bill already paid!", "warning");
    } else {
      let customerEmail;
      this.api.getCustomerById(bill.customerId).subscribe((response: any) => {
        customerEmail = response.data.email;
        this.paymentService
            .createPaymentLink(
              customerEmail,
              bill.amount,
              true, // all payment methods enabled...
              bill.id
            )
            .subscribe((response) => {
              console.log(response);
              Swal.fire("Success", "Payment Link Sent!", "success");
            });
      });
    }
  }

  searchBills() {
    this.fetchBills(this.auth.currentUserId, this.searchTerm);
  }

  editBill(id: string) {
    //...
  }

  deleteBill(id: string) {
    Swal.fire({
      title: "Are you sure?",
      text: "Do you want to delete the bill?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Yes, delete it!"
    }).then((result) => {
      if (result.isConfirmed) {
        this.api.deleteBillById(id).subscribe(() => {
          Swal.fire("Success", "Bill deleted successfully!", "success");
          this.fetchBills(this.auth.currentUserId);
        });
      }
    });
  }

  previousPage() {
    if (!this.page.first) {
      this.fetchBills(this.auth.currentUserId, this.searchTerm, this.page.number - 1);
    }
  }

  nextPage() {
    if (!this.page.last) {
      this.fetchBills(this.auth.currentUserId, this.searchTerm, this.page.number + 1);
    }
  }
}
