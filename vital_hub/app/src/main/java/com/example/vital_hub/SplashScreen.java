package com.example.vital_hub;

import static com.example.vital_hub.client.spring.controller.Api.initRetrofitAndController;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.vital_hub.authentication.LoginScreen;

public class SplashScreen extends Activity {
    Handler handler;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        initRetrofitAndController(prefs.getString("server", "10.0.2.2"));
        setContentView(R.layout.splash_screen);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
