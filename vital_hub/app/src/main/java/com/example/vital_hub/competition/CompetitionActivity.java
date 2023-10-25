package com.example.vital_hub.competition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.example.vital_hub.R;
import com.example.vital_hub.profile.UserProfile;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.client.controller.Api;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.vital_hub.client.objects.CompetitionListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.vital_hub.helper.*;

public class CompetitionActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    private Boolean isJoined = false;
    private int limit = 10;
    private int offset = 0;
    private RecyclerView competitionList;
    private CompetitionListAdapter competitionListAdapter;
    private Button suggestButton, isJoinedButton, findButton, addButton;
    private CompetitionListResponse competitionListResponse;
    private ArrayList<Competition> competitions;
    private LinearLayoutManager layoutManager;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competition_layout);

        //NavBar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.competition);

        // Helper
        KeyboardHelper.setupKeyboardHiding(this);

        // Init header for request
        initHeaderForRequest();

        // Button
        suggestButton = findViewById(R.id.suggest);
        isJoinedButton = findViewById(R.id.joined);
        suggestButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9A9A9")));
        suggestButton.setTextColor(Color.parseColor("#000000"));

        // Change color of button when clicked
        suggestButton.setOnClickListener(v -> {

            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#1DB964"));
            isJoinedButton.setBackgroundTintList(colorStateList);
            isJoinedButton.setTextColor(Color.parseColor("#FFFFFF"));

            ColorStateList colorStateList2 = ColorStateList.valueOf(Color.parseColor("#A9A9A9"));
            suggestButton.setBackgroundTintList(colorStateList2);
            suggestButton.setTextColor(Color.parseColor("#000000"));
            isJoined = false;
            reloadData();
        });

        isJoinedButton.setOnClickListener(v -> {

            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#A9A9A9"));
            isJoinedButton.setBackgroundTintList(colorStateList);
            isJoinedButton.setTextColor(Color.parseColor("#000000"));

            ColorStateList colorStateList2 = ColorStateList.valueOf(Color.parseColor("#1DB964"));
            suggestButton.setBackgroundTintList(colorStateList2);
            suggestButton.setTextColor(Color.parseColor("#FFFFFF"));
            isJoined = true;
            reloadData();
        });

        // Get competition list
        competitions = new ArrayList<>();
        competitionList = findViewById(R.id.competition_list);
        competitionList.setHasFixedSize(true);
        fetchCompetitionList(isJoined, null, limit, offset);
        competitionListAdapter = new CompetitionListAdapter(competitions);
        competitionList.setAdapter(competitionListAdapter);
        layoutManager = new LinearLayoutManager(this);
        competitionList.setLayoutManager(layoutManager);

        // Lazy load
        competitionList.addOnScrollListener(
                new EndlessScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore() {
                        offset += limit;
                        fetchCompetitionList(isJoined, null, limit, offset);
                    }
                }
        );


    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchCompetitionList(Boolean isJoined, String name, Integer limit, Integer offset) {
        Api.initGetCompetitionList(headers, isJoined, name, limit, offset);
        Api.getCompetitionList.enqueue(new Callback<CompetitionListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CompetitionListResponse> call, @NonNull Response<CompetitionListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    competitionListResponse = response.body();
                    assert competitionListResponse != null;
                    competitions.addAll(Arrays.asList(competitionListResponse.getData()));
                    competitionListAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Error", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompetitionListResponse> call, @NonNull Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
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
            startActivity(new Intent(getApplicationContext(), ExerciseGeneralActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.competition) {
            return true;
        } else {
            return false;
        }
    }

    private void reloadData() {
        competitions.clear();
        competitionListAdapter.notifyDataSetChanged();

        offset = 0;

        fetchCompetitionList(isJoined, null, limit, offset);
    }
}
