package com.main.server.apiExceptionHandler.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int errorStatus;
    private String message;
}
