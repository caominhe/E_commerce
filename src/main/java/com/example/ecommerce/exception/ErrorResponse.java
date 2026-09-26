package com.example.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private String path;
}