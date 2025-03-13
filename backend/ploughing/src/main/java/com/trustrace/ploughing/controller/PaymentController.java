package com.trustrace.ploughing.controller;

import com.trustrace.ploughing.dto.ApiResponse;
import com.trustrace.ploughing.model.Payment;
import com.trustrace.ploughing.service.EmailService;
import com.trustrace.ploughing.service.PaymentService;
import com.trustrace.ploughing.service.RazorpayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${ploughing.build.version}/api/payments")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/link")
    public String createPaymentLink(@RequestBody Map<String, Object> requestBody) {
        try {
            String email = (String) requestBody.get("email");
            double amount = Double.parseDouble(requestBody.get("amount").toString());
            boolean allMethods = (boolean) requestBody.get("allMethods");
            String billId = (String) requestBody.get("billId");

            String paymentLink = razorpayService.createPaymentLink(email, amount, allMethods, billId);
            //emailService.sendPaymentLink(email, paymentLink);

            System.out.println("Payment link: " + paymentLink);

            return "Payment link created successfully: " + paymentLink;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> savePayment(@RequestBody Payment payment) {
        Payment savedPayment = paymentService.savePayment(payment);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment created successfully", savedPayment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable String id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment retrieved successfully", payment));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(new ApiResponse<>(true, "Payments retrieved successfully", payments));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByOwnerId(@PathVariable String ownerId) {
        List<Payment> payments = paymentService.getPaymentsByOwnerId(ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payments retrieved successfully", payments));
    }
}
