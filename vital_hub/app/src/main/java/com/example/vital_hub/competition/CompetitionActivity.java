package com.example.vital_hub.competition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CompetitionListResponse;
import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.example.vital_hub.helper.EndlessScrollListener;
import com.example.vital_hub.helper.KeyboardHelper;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.profile.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    private Boolean isJoined = false, prevIsJoined = false, isCreated = false, prevIsCreated = false;
    private int limit = 10;
    private int offset = 0;
    private RecyclerView competitionList;
    private CompetitionListAdapter competitionListAdapter;
    private Button suggestButton, isJoinedButton, isCreatedButton;
    private ImageButton addButton;
    private EditText searchCompetition;
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

        // NavBar
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
        isCreatedButton = findViewById(R.id.created);
        suggestButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9A9A9")));
        suggestButton.setTextColor(Color.parseColor("#000000"));

        // Change color of button when clicked
        suggestButton.setOnClickListener(v -> {
            KeyboardHelper.hideKeyboard(this);
            if (searchCompetition.getText().toString().length() > 0) {
                searchCompetition.setText("");
            }
            searchCompetition.clearFocus();
            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#1DB964"));
            isJoinedButton.setBackgroundTintList(colorStateList);
            isJoinedButton.setTextColor(Color.parseColor("#FFFFFF"));

            isCreatedButton.setBackgroundTintList(colorStateList);
            isCreatedButton.setTextColor(Color.parseColor("#FFFFFF"));

            ColorStateList colorStateList2 = ColorStateList.valueOf(Color.parseColor("#A9A9A9"));
            suggestButton.setBackgroundTintList(colorStateList2);
            suggestButton.setTextColor(Color.parseColor("#000000"));
            prevIsJoined = isJoined;
            isJoined = false;

            prevIsCreated = isCreated;
            isCreated = false;
            reloadData();
        });

        isJoinedButton.setOnClickListener(v -> {
            KeyboardHelper.hideKeyboard(this);
            if (searchCompetition.getText().toString().length() > 0) {
                searchCompetition.setText("");
            }
            searchCompetition.clearFocus();
            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#A9A9A9"));
            isJoinedButton.setBackgroundTintList(colorStateList);
            isJoinedButton.setTextColor(Color.parseColor("#000000"));

            ColorStateList colorStateList2 = ColorStateList.valueOf(Color.parseColor("#1DB964"));
            suggestButton.setBackgroundTintList(colorStateList2);
            suggestButton.setTextColor(Color.parseColor("#FFFFFF"));

            isCreatedButton.setBackgroundTintList(colorStateList2);
            isCreatedButton.setTextColor(Color.parseColor("#FFFFFF"));

            prevIsCreated = isCreated;
            isCreated = false;
            prevIsJoined = isJoined;
            isJoined = true;
            reloadData();
        });

        isCreatedButton.setOnClickListener(v -> {
            KeyboardHelper.hideKeyboard(this);
            if (searchCompetition.getText().toString().length() > 0) {
                searchCompetition.setText("");
            }
            searchCompetition.clearFocus();
            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#A9A9A9"));
            isCreatedButton.setBackgroundTintList(colorStateList);
            isCreatedButton.setTextColor(Color.parseColor("#000000"));

            ColorStateList colorStateList2 = ColorStateList.valueOf(Color.parseColor("#1DB964"));
            suggestButton.setBackgroundTintList(colorStateList2);
            suggestButton.setTextColor(Color.parseColor("#FFFFFF"));

            isJoinedButton.setBackgroundTintList(colorStateList2);
            isJoinedButton.setTextColor(Color.parseColor("#FFFFFF"));

            prevIsJoined = isJoined;
            isJoined = false;
            prevIsCreated = isCreated;
            isCreated = true;
            reloadData();
        });

        // Search
        searchCompetition = findViewById(R.id.search);
        searchCompetition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                competitions.clear();
                if (isCreated) {
                    fetchOwnCompetitionList(s.toString(), limit, offset);
                }
                else fetchCompetitionList(isJoined, s.toString(), limit, offset);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Add button
        addButton = findViewById(R.id.add_competition);
        addButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AddCompeActivity.class));
        });

        // Get competition list
        competitions = new ArrayList<>();
        competitionList = findViewById(R.id.competition_list);
        competitionList.setHasFixedSize(true);
        if (isCreated) {
            fetchOwnCompetitionList(null, limit, offset);
        }
        else fetchCompetitionList(isJoined, null, limit, offset);
        competitionListAdapter = new CompetitionListAdapter(competitions, isJoined, isCreated);
        competitionList.setAdapter(competitionListAdapter);
        layoutManager = new LinearLayoutManager(this);
        competitionList.setLayoutManager(layoutManager);

        // Lazy load
        competitionList.addOnScrollListener(
                new EndlessScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore() {
                        offset += limit;
                        if (isCreated) {
                            fetchOwnCompetitionList(searchCompetition.getText().toString(), limit, offset);
                        }
                        else fetchCompetitionList(isJoined, searchCompetition.getText().toString(), limit, offset);
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
                    competitionListAdapter = new CompetitionListAdapter(competitions, isJoined, isCreated);
                    competitionList.setAdapter(competitionListAdapter);
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

    private void fetchOwnCompetitionList(String name, Integer limit, Integer offset) {
        Api.initGetOwnCompetitionList(headers, name, limit, offset);
        Api.getOwnCompetitionList.enqueue(new Callback<CompetitionListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CompetitionListResponse> call, @NonNull Response<CompetitionListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    competitionListResponse = response.body();
                    assert competitionListResponse != null;
                    competitions.addAll(Arrays.asList(competitionListResponse.getData()));
                    competitionListAdapter = new CompetitionListAdapter(competitions, isJoined, isCreated);
                    competitionList.setAdapter(competitionListAdapter);
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
        offset = 0;
        if (isCreated && !prevIsCreated) {
            competitions.clear();
            fetchOwnCompetitionList(null, limit, offset);
        }
        else if (isJoined != prevIsJoined || isCreated != prevIsCreated) {
            competitions.clear();
            fetchCompetitionList(isJoined, null, limit, offset);
        }
    }
}
