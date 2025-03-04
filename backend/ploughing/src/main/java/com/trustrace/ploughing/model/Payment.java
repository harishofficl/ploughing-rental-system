package com.trustrace.ploughing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String paymentId;
    private String paymentLinkId;
    private String ownerId;
    private String customerName;
    private String billId;
    private double totalAmount;
    private String status;
    private String signature;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Payment(String paymentId, String paymentLinkId, String ownerId, String customerName, String billId, double totalAmount, String status, String signature) {
        this.paymentId = paymentId;
        this.paymentLinkId = paymentLinkId;
        this.ownerId = ownerId;
        this.customerName = customerName;
        this.billId = billId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.signature = signature;
    }
}
