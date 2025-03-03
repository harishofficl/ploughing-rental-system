package com.trustrace.ploughing.service;

import com.razorpay.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    public String createPaymentLink(String customerEmail, double amount, boolean allMethods) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount * 100);
        paymentLinkRequest.put("currency", "INR");
        paymentLinkRequest.put("description", "Payment for ploughing service");

        JSONObject customer = new JSONObject();
        customer.put("email", customerEmail);
        paymentLinkRequest.put("customer", customer);
        paymentLinkRequest.put("notify", new JSONObject().put("email", true));
        paymentLinkRequest.put("callback_url", "https://75d6-49-204-235-237.ngrok-free.app/v1/api/payment/webhook");
        paymentLinkRequest.put("callback_method", "get");

        if (!allMethods) {
            // only UPI implemented
            System.out.println("Only UPI implemented");

//            paymentLinkRequest.put("options", new JSONObject()
//                    .put("payment_methods", new JSONObject()
//                            .put("upi", true)
//                            .put("card", false)
//                            .put("netbanking", false)
//                            .put("wallet", false)));
        }

        PaymentLink payment;
        try {
            payment = razorpay.paymentLink.create(paymentLinkRequest);
            return payment.get("short_url");
        } catch (RazorpayException e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }
}