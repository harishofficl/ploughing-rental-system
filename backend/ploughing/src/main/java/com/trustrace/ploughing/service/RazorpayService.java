package com.trustrace.ploughing.service;

import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    public String createPaymentLink(String customerEmail, double amount) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount * 100);
        paymentLinkRequest.put("currency", "INR");
        paymentLinkRequest.put("description", "Payment for ploughing service");

        JSONObject customer = new JSONObject();
        customer.put("email", customerEmail);

        paymentLinkRequest.put("customer", customer);
        paymentLinkRequest.put("notify", new JSONObject().put("email", true));
        paymentLinkRequest.put("callback_url", "https://d898-103-130-204-15.ngrok-free.app/v1/api/payment/callback");
        paymentLinkRequest.put("callback_method", "get");

        PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
        return payment.get("short_url");
    }
}
