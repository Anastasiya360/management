package com.example.management.exceptoin;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ApiException extends RuntimeException {
    private int statusCode;

    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
