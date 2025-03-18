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

    @PostMapping("/redirect")
    public String handlePaymentRedirect(@RequestBody Map<String, String> paymentBody) {
//        String paymentId = paymentBody.get("razorpayPaymentId");
//        String paymentLinkId = paymentBody.get("razorpayPaymentLinkId");
//        String status = paymentBody.get("razorpayStatus");
//        String signature = paymentBody.get("razorpaySignature");
//
//        Bill bill = billService.findByPaymentId(paymentLinkId);
//        Payment payment = null;
//        if(bill != null && status.equals("paid")) {
//            billService.setBillRentalPaid(bill.getId());
//            payment = paymentService.savePayment(new Payment(paymentId, paymentLinkId, bill.getOwnerId(), bill.getCustomerName(), bill.getId(), bill.getTotalAmount(), status, signature));
//        }
//        assert payment != null;
        return new JSONObject().put("Payment Status", "Success").toString();
    }

    @PostMapping("/webhook")
    public void handleRazorPayWebhook(@RequestBody Map<String, Object> requestBody) {

        try {
            // Extract the "payload" object
            Map<String, Object> payload = (Map<String, Object>) requestBody.get("payload");
            if (payload == null) {
                log.error("Invalid payload in webhook");
                return;
            }

            // Extract payment entity
            Map<String, Object> paymentData = (Map<String, Object>) payload.get("payment");
            if (paymentData == null) {
                log.error("Payment data missing in payload");
                return;
            }

            Map<String, Object> paymentEntity = (Map<String, Object>) paymentData.get("entity");
            if (paymentEntity == null) {
                log.error("Payment entity missing in webhook payload");
                return;
            }

            // Extract payment_link entity
            Map<String, Object> paymentLinkData = (Map<String, Object>) payload.get("payment_link");
            if (paymentLinkData == null) {
                log.error("Payment link data missing in payload");
                return;
            }

            Map<String, Object> paymentLinkEntity = (Map<String, Object>) paymentLinkData.get("entity");
            if (paymentLinkEntity == null) {
                log.error("Payment link entity missing in payload");
                return;
            }

            String paymentId = (String) paymentEntity.get("id");
            String paymentLinkId = (String) paymentLinkEntity.get("id");
            String status = (String) paymentEntity.get("status");
            double totalAmount = ((Number) paymentEntity.get("amount")).doubleValue() / 100;

            log.info("WebHook Received for payment link ID: {}", paymentLinkId);

            Bill bill = billService.findByPaymentId(paymentLinkId);
            if (bill != null && "captured".equals(status)) {
                billService.setBillRentalPaid(bill.getId());

                Payment payment = new Payment(
                        paymentId,
                        paymentLinkId,
                        bill.getOwnerId(),
                        bill.getCustomerName(),
                        bill.getId(),
                        totalAmount,
                        "paid",
                        "" // Signature not provided in webhook
                );

                paymentService.savePayment(payment);
            } else {
                log.error("Bill not found or payment status is not 'captured'");
            }
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
        }
    }

}
