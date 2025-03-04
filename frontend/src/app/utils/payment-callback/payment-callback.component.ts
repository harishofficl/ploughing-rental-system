import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PaymentService } from '../../services/payment/payment.service';

@Component({
  selector: 'app-payment-callback',
  standalone: true,
  imports: [],
  templateUrl: './payment-callback.component.html',
  styleUrl: './payment-callback.component.css',
})
export class PaymentCallbackComponent implements OnInit {

  paymentBody: any;

  constructor(private paymentService: PaymentService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const razorpayPaymentId = params['razorpay_payment_id'];
      const razorpayPaymentLinkId = params['razorpay_payment_link_id'];
      const razorpaySignature = params['razorpay_signature'];
      const razorpayStatus = params['razorpay_payment_link_status'];
      
      this.paymentBody = {
        razorpayPaymentId,
        razorpayPaymentLinkId,
        razorpayStatus,
        razorpaySignature,
      };

      this.paymentService.paymentWebHookCallBack(this.paymentBody).subscribe((response) => {
        console.log(response);
      });
    });
  }

  closetab() {
    if (window.opener) {
      window.close();
    }
  }
}