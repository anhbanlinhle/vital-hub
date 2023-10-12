package com.example.vital_hub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.post_comment.PostCommentActivity;

public class SplashScreen extends Activity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
