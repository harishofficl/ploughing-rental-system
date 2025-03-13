package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.model.Bill;
import com.trustrace.ploughing.model.Payment;
import com.trustrace.ploughing.service.BillService;
import com.trustrace.ploughing.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${ploughing.build.version}/api/payment")
public class PaymentCallBackController {

    @Autowired
    private BillService billService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody Map<String, String> paymentBody) {
        String paymentId = paymentBody.get("razorpayPaymentId");
        String paymentLinkId = paymentBody.get("razorpayPaymentLinkId");
        String status = paymentBody.get("razorpayStatus");
        String signature = paymentBody.get("razorpaySignature");

        Bill bill = billService.findByPaymentId(paymentLinkId);
        Payment payment = null;
        if(bill != null && status.equals("paid")) {
            billService.setBillRentalPaid(bill.getId());
            payment = paymentService.savePayment(new Payment(paymentId, paymentLinkId, bill.getOwnerId(), bill.getCustomerName(), bill.getId(), bill.getTotalAmount(), status, signature));
        }
        assert payment != null;
        return new JSONObject().put("Payment Status", "Success").toString();
    }
}
