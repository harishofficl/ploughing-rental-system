package com.trustrace.ploughing.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillView {
    private String id;
    private String customerName;
    private String date;
    private int totalRentalRecords;
    private double amount;
    private boolean paid;
    private String paymentLinkId;
    private String customerId;
}
