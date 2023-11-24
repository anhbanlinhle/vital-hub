package com.example.vital_hub.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CompetitionHistoryListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionHistoryActivity extends AppCompatActivity {
    ImageView backButton;
    RecyclerView competitionHistoryList;
    CompetitionHistoryListAdapter competitionHistoryListAdapter;
    List<CompetitionHistoryListResponse> competitionHistoryListResponse;
    ArrayList <CompetitionHistoryListResponse> competitionHistories;
    EditText searchBar;
    LinearLayoutManager layoutManager;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    Integer pageNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        initHeaderForRequest();

        competitionHistories = new ArrayList<>();
        competitionHistoryList = findViewById(R.id.competition_history_list);
        backButton = findViewById(R.id.back_button);
        searchBar = findViewById(R.id.search);
        competitionHistoryListAdapter = new CompetitionHistoryListAdapter(competitionHistories);
        competitionHistoryList.setAdapter(competitionHistoryListAdapter);
        layoutManager = new LinearLayoutManager(this);
        competitionHistoryList.setLayoutManager(layoutManager);
        fetchCompetitionHistoryList(pageNum);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchCompetitionHistoryList(Integer pageNum) {
        Api.initGetCompetitionHistoryList(headers, pageNum);
        Api.getCompetitionHistoryList.clone().enqueue(new Callback<List<CompetitionHistoryListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CompetitionHistoryListResponse>> call, @NonNull Response<List<CompetitionHistoryListResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    competitionHistoryListResponse = response.body();
                    assert competitionHistoryListResponse != null;
                    competitionHistories.addAll(competitionHistoryListResponse);
                    competitionHistoryListAdapter = new CompetitionHistoryListAdapter(competitionHistories);
                    competitionHistoryList.setAdapter(competitionHistoryListAdapter);
                } else {
                    Log.d("Error", "Error: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<CompetitionHistoryListResponse>> call,@NonNull Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }

        });
    }


}
