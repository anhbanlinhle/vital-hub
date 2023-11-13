package com.example.vital_hub.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vital_hub.R;
import com.example.vital_hub.bicycle.BicycleTracker;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.pushup.PushupVideoScan;
import com.example.vital_hub.profile.UserProfile;
import com.example.vital_hub.test.TestMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ExerciseGeneralActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    private ImageButton chooseGymBtn;
    private ImageButton chooseRunBtn;
    private ImageButton chooseBicycleBtn;
    private ImageButton choosePushupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_general_layout);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.exercise);

        chooseGymBtn = findViewById(R.id.choose_gym_btn);
        chooseRunBtn = findViewById(R.id.choose_run_btn);
        choosePushupBtn = findViewById(R.id.choose_pushup_btn);
        chooseBicycleBtn = findViewById(R.id.choose_bicycle_btn);

        btnBinding();

    }

    private void btnBinding() {
        chooseGymBtn.setOnClickListener(v -> {
            startActivity(new Intent(ExerciseGeneralActivity.this, ChooseExerciseActivity.class));
        });
        choosePushupBtn.setOnClickListener(v -> {
            startActivity(new Intent(ExerciseGeneralActivity.this, PushupVideoScan.class));
        });
        chooseBicycleBtn.setOnClickListener(v -> {
            startActivity(new Intent(ExerciseGeneralActivity.this, BicycleTracker.class));
        });
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
