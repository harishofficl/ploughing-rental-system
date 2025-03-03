package com.trustrace.ploughing.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalRecordView {
    private String id;
    private String ownerId;
    private String customerName;
    private String driverName;
    private String date;
    private String equipment;
    private double hoursUsed;
    private double totalCost;
}
