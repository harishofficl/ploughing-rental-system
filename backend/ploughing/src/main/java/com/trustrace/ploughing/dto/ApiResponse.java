package com.trustrace.ploughing.dto;

public record ApiResponse<T>(boolean success, String message, T data) { }

