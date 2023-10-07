package com.main.server.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse {
    String message;
    Boolean success;
    JSONObject data;
}
