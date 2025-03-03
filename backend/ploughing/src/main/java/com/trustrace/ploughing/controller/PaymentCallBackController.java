package com.trustrace.ploughing.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${survey.build.version}/api/payment")
public class PaymentCallBackController {

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Webhook received: " + payload);

        String event = (String) payload.get("event");

        System.out.println("Event: " + event);

        if ("payment_link.paid".equals(event) || "payment.captured".equals(event)) {
            Map<String, Object> paymentData = (Map<String, Object>) payload.get("payload");
            Map<String, Object> payment = (Map<String, Object>) paymentData.get("payment");

            String paymentId = (String) payment.get("id");
            String status = (String) payment.get("status");

            System.out.println("Payment ID: " + paymentId);
            System.out.println("Status: " + status);

            // ✅ Update database, mark rental record as paid


        }

        return "Webhook received successfully!";
    }
}
