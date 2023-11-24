package com.example.vital_hub.pushup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.Button;

import com.example.vital_hub.R;

public class PushUpPoseActivity extends AppCompatActivity {

    AppCompatButton back;

    Button understood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up_pose);

        back = findViewById(R.id.back);
        understood = findViewById(R.id.understood);

        back.setOnClickListener(v -> {
            finish();
        });

        understood.setOnClickListener(v -> {
            finish();
        });
    }
}