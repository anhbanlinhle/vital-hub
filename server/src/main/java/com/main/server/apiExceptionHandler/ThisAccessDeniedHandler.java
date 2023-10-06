package com.main.server.apiExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.server.apiExceptionHandler.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class ThisAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            try {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                ErrorResponse errorResponse = new ErrorResponse(403, "No authorization");
                byte[] responseToSend = restResponseBytes(errorResponse);
                response.setHeader("Content-Type", "application/json");
                response.setStatus(403);
                response.getOutputStream().write(responseToSend);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
        return serialized.getBytes();
    }
}
