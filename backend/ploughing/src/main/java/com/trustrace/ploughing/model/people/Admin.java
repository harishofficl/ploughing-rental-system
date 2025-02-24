package com.trustrace.ploughing.model.people;

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
@Document(collection = "admins")
public class Admin {
    @Id
    private String id;
    private String name;
    private String phNo;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private List<String> roles;
    private boolean active;
}
