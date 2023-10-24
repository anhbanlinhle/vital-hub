package com.example.vital_hub.test;

import static com.example.vital_hub.authentication.LoginScreen.oneTapClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.example.vital_hub.authentication.LoginScreen;
import com.example.vital_hub.R;
import com.example.vital_hub.UserProfile;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class TestMain extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    TextView email;
    TextView displayName;
    Button logoutBtn;

    BottomNavigationView bottomNavigationView;
    Button fetch;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);

        prefs = getSharedPreferences("UserData", MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        email = findViewById(R.id.email);
        displayName = findViewById(R.id.displayName);
        logoutBtn = findViewById(R.id.btnLogout);
        fetch = findViewById(R.id.fetch);

        email.setText(prefs.getString("email", "null"));
        displayName.setText(prefs.getString("name", "null"));

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestMain.this, TestFetch.class);
                startActivity(intent);
            }
        });
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
        Intent intent = new Intent(TestMain.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), UserProfile.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.home) {
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.exercise) {
            startActivity(new Intent(getApplicationContext(), ExerciseGeneralActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.competition) {
            startActivity(new Intent(getApplicationContext(), CompetitionActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else {
            return false;
        }
    }

}