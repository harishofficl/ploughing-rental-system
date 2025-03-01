import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private url = 'http://localhost:8080/v1';

  constructor(private http: HttpClient) {}

  createPaymentLink(email: string, amount: number) {
    const payload = { email, amount };
    return this.http.post(`${this.url}/api/payment/link`, payload, { responseType: 'text' });
  }
}
