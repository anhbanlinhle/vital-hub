package com.main.server.security;

import com.main.server.utils.response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class TokenResponse extends Response {
    private String token;
    private Boolean isFirstSign;

    public TokenResponse (String token, Boolean isFirstSign, String msg, Integer status) {
        super(msg, status);
        this.token = token;
        this.isFirstSign = isFirstSign;
    }
}
