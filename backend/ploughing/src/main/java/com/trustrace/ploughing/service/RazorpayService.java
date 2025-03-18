package com.trustrace.ploughing.service;

import com.razorpay.*;
import com.trustrace.ploughing.dao.BillDao;
import com.trustrace.ploughing.model.Bill;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RazorpayService {

    @Autowired
    private BillDao billDao;

    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    public String createPaymentLink(String customerEmail, double amount, boolean allMethods, String billId) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount * 100);
        paymentLinkRequest.put("currency", "INR");
        paymentLinkRequest.put("description", "Payment for ploughing service");

        JSONObject customer = new JSONObject();
        customer.put("email", customerEmail);
        paymentLinkRequest.put("customer", customer);
        paymentLinkRequest.put("notify", new JSONObject().put("email", true));
        paymentLinkRequest.put("callback_url", "http://localhost:4200/payment/callback");
        paymentLinkRequest.put("callback_method", "get");

        if (!allMethods) {
            JSONObject instruments = new JSONObject();
            instruments.put("method", "upi");

            JSONObject banks = new JSONObject();
            banks.put("name", "Pay using UPI");
            banks.put("instruments", new JSONArray().put(instruments));

            JSONObject blocks = new JSONObject();
            blocks.put("banks", banks);

            JSONObject display = new JSONObject();
            display.put("blocks", blocks);
            display.put("sequence", new JSONArray().put("block.banks"));
            display.put("preferences", new JSONObject().put("show_default_blocks", false));

            JSONObject config = new JSONObject();
            config.put("display", display);

            JSONObject checkout = new JSONObject();
            checkout.put("config", config);

            JSONObject options = new JSONObject();
            options.put("checkout", checkout);

            paymentLinkRequest.put("options", options);
        }
        PaymentLink payment;
        try {
            payment = razorpay.paymentLink.create(paymentLinkRequest);
            String paymentId = payment.get("id");
            if(billDao.findById(billId).isPresent()){
                Bill bill = billDao.findById(billId).get();
                if(!bill.getPaymentId().isEmpty()) {
                    log.info("Cancel existing payment link: {}", bill.getPaymentId());
                    cancelPaymentLink(bill.getPaymentId());
                }
                billDao.updatePaymentId(billId, paymentId);
            }
            return payment.get("short_url");
        } catch (RazorpayException e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    public void cancelPaymentLink(String paymentLinkId) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);
            PaymentLink paymentLink = razorpay.paymentLink.cancel(paymentLinkId);
            log.info("Payment link: {} canceled successfully.", paymentLinkId);
        } catch (RazorpayException e) {
            log.error("Error canceling payment link {}: {}", paymentLinkId, e.getMessage());
        }
    }
}