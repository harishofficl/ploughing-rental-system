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

            String paymentLink = razorpayService.createPaymentLink(email, amount);
            //emailService.sendPaymentLink(email, paymentLink);

            return "Payment link sent successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
