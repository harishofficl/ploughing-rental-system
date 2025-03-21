package com.trustrace.ploughing.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetail {
    private String id;
    private String name;
    private String role;
}
