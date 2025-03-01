package com.trustrace.ploughing.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bills")
public class Bill {
    @Id private String id;
    private String ownerId;
    private String customerId;
    private double totalAmount;
    private LocalDateTime createdAt;
    private List<String> rentalRecordIds;
    private boolean paid;
}
