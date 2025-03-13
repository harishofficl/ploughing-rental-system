package com.trustrace.ploughing.security.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String userId;
    private String tokenType;

    public AuthResponseDto(String accessToken, String userId) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.tokenType = "Bearer ";
    }

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
        this.userId = "";
        this.tokenType = "Bearer ";
    }
}
