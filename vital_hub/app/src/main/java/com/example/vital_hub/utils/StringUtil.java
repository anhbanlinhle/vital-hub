package com.example.vital_hub.utils;

import android.text.Editable;

public class StringUtil {
    public static boolean isEmpty (String s) {
        return (s == null || s.equals(""));
    }

    public static boolean isEmpty (Editable editable) {
        if (editable == null) {
            return true;
        } else {
            return isEmpty(editable.toString());
        }
    }
}
