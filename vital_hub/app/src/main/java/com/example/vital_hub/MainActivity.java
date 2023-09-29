package com.example.vital_hub;

import static com.example.vital_hub.LoginScreen.oneTapClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;


    
public class MainActivity extends AppCompatActivity {
    Button button;
    TextView email;
    TextView displayName;
    Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        displayName = findViewById(R.id.displayName);
        logoutBtn = findViewById(R.id.btnLogout);

        Intent intent = getIntent();
        email.setText(intent.getStringExtra("email"));
        displayName.setText(intent.getStringExtra("name"));

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void signOut() {
        oneTapClient.signOut();
        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(MainActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

}