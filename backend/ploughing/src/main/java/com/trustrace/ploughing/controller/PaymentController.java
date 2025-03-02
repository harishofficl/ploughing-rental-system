package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.service.EmailService;
import com.trustrace.ploughing.service.RazorpayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${survey.build.version}/api/payment")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/link")
    public String createPaymentLink(@RequestBody Map<String, Object> requestBody) {
        try {
            String email = (String) requestBody.get("email");
            double amount = Double.parseDouble(requestBody.get("amount").toString());
            boolean allMethods = (boolean) requestBody.get("allMethods");

            System.out.println("Email: " + email);
            System.out.println("Amount: " + amount);
            System.out.println("All methods: " + allMethods);

            String paymentLink = razorpayService.createPaymentLink(email, amount, allMethods);
            //emailService.sendPaymentLink(email, paymentLink);

            System.out.println("Payment link: " + paymentLink);

            return "Payment link created successfully: " + paymentLink;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
