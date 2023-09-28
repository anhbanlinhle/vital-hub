package com.example.vital_hub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView email;
    TextView displayName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        displayName = findViewById(R.id.displayName);

        Intent intent = getIntent();
        email.setText(intent.getStringExtra("email"));
        displayName.setText(intent.getStringExtra("displayName"));


    }
}