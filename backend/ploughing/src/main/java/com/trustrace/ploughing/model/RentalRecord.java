package com.trustrace.ploughing.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rental_records")
public class RentalRecord {
    @Id private String id;
    private String ownerId;
    private String driverId;
    private String customerId;
    private String equipment;
    private Date date;
    private double hoursUsed;
    private double ratePerHour;
    private double totalCost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean paid = false;
}
