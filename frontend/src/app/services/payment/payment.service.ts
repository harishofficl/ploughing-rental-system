import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private url = 'http://localhost:8080/v1';

  constructor(private http: HttpClient) {}

  createPaymentLink(email: string, amount: number, allMethods: boolean) {
    const payload = { email, amount, allMethods };
    return this.http.post(`${this.url}/api/payment/link`, payload, { responseType: 'text' });
  }
}