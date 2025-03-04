import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError } from 'rxjs';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private url = 'http://localhost:8080/v1';

  constructor(private http: HttpClient) {}

  createPaymentLink(email: string, amount: number, allMethods: boolean, billId: string) {
    const payload = { email, amount, allMethods, billId };
    return this.http.post(`${this.url}/api/payments/link`, payload, { responseType: 'text' });
  }

  paymentWebHookCallBack(paymentbody: any) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.url}/api/payment/webhook`, paymentbody, { headers });
  }
}