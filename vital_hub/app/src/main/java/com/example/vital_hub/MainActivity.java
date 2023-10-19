package com.example.vital_hub;

import static com.example.vital_hub.LoginScreen.oneTapClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    Button userprofileBtn;
    TextView email;
    TextView displayName;
    Button logoutBtn;

//    BottomNavigationView bottomNavigationView;
    Button fetch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.home);

        userprofileBtn = findViewById(R.id.btnUserprofile);
        email = findViewById(R.id.email);
        displayName = findViewById(R.id.displayName);
        logoutBtn = findViewById(R.id.btnLogout);
        fetch = findViewById(R.id.fetch);

        Intent intent = getIntent();
        email.setText(intent.getStringExtra("email"));
        displayName.setText(intent.getStringExtra("name"));



        //Open profile page on button click
        userprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserprofie();
            }
        });
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Fetch.class);
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
        Intent intent = new Intent(MainActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    public void openUserprofie(){
        Intent intent = new Intent(MainActivity.this, UserProfile.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.profile) {
//            startActivity(new Intent(getApplicationContext(), UserProfile.class));
//            overridePendingTransition(0, 0);
//            return true;
//        } else if (item.getItemId() == R.id.home) {
//            return true;
//        } else if (item.getItemId() == R.id.exercise) {
//            startActivity(new Intent(getApplicationContext(), ExerciseActivity.class));
//            overridePendingTransition(0, 0);
//            return true;
//        } else if (item.getItemId() == R.id.competition) {
//            startActivity(new Intent(getApplicationContext(), CompetitionActivity.class));
//            overridePendingTransition(0, 0);
//            return true;
//        } else {
//            return false;
//        }
//    }

}