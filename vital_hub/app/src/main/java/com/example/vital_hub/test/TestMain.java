package com.example.vital_hub.test;

import static com.example.vital_hub.authentication.LoginScreen.oneTapClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.example.vital_hub.authentication.LoginScreen;
import com.example.vital_hub.R;
import com.example.vital_hub.profile.UserProfile;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.pushup.PushupVideoScan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jakewharton.processphoenix.ProcessPhoenix;


public class TestMain extends AppCompatActivity {
    TextView email;
    TextView displayName;
    TextView server;
    Button logoutBtn;
    Button fetch;
    Button restart;
    Button changeIp;
    Button function;
    Button back;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
        Toast.makeText(this, "Developer mode", Toast.LENGTH_LONG).show();

        prefs = getSharedPreferences("UserData", MODE_PRIVATE);

        email = findViewById(R.id.email);
        displayName = findViewById(R.id.displayName);
        server = findViewById(R.id.serverAddress);
        logoutBtn = findViewById(R.id.btnLogout);
        fetch = findViewById(R.id.fetch);
        restart = findViewById(R.id.restart);
        changeIp = findViewById(R.id.changeIp);
        function = findViewById(R.id.function);
        back = findViewById(R.id.back);

        email.setText(prefs.getString("email", "null"));
        displayName.setText(prefs.getString("name", "null"));
        server.setText(prefs.getString("server", "default"));


        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestMain.this, TestFetch.class);
                startActivity(intent);
            }
        });
        changeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestMain.this, TestServer.class);
                startActivity(intent);
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessPhoenix.triggerRebirth(getApplicationContext());
            }
        });
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestMain.this, TestNav.class);
                startActivity(intent);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Developer mode", Toast.LENGTH_LONG).show();
        email.setText(prefs.getString("email", "null"));
        displayName.setText(prefs.getString("name", "null"));
        server.setText(prefs.getString("server", "default"));
    }

    private void signOut() {
        oneTapClient.signOut();
        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(TestMain.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

}