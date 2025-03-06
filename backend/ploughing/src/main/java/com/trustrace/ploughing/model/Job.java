package com.trustrace.ploughing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job {
    @Id private String id;
    private String ownerId;
    private String driverId;
    private String vehicleId;
    private String customerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalTimeInSeconds = 0;
    private String startImagePath;
    private String endImagePath;
    private double dieselUsed = 0.0;
    private boolean completed = false;
}
