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
@Document(collection = "gps")
public class Gps {
    @Id private String id;
    private String driverId;
    private String latitude;
    private String longitude;
    private String timeStamp;
}
