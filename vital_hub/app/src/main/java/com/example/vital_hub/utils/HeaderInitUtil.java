package com.example.vital_hub.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class HeaderInitUtil {

    public static Map<String, String> headerWithToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreferences.getString("jwt", null));
        return header;
    }
}
