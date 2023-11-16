package com.example.vital_hub.competition;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CompetitionListResponse;
import com.example.vital_hub.helper.EndlessScrollListener;
import com.example.vital_hub.helper.KeyboardHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionFragment extends Fragment {
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
    public CompetitionFragment() {
        // Required empty public constructor
    }

    public static CompetitionFragment newInstance(String param1, String param2) {
        CompetitionFragment fragment = new CompetitionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_competition, container, false);

        // Helper
        KeyboardHelper.setupKeyboardHiding(this.getActivity());

        // Init header for request
        initHeaderForRequest();

        // Button
        suggestButton = view.findViewById(R.id.suggest);
        isJoinedButton = view.findViewById(R.id.joined);
        isCreatedButton = view.findViewById(R.id.created);
        suggestButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9A9A9")));
        suggestButton.setTextColor(Color.parseColor("#000000"));

        // Change color of button when clicked
        suggestButton.setOnClickListener(v -> {
            KeyboardHelper.hideKeyboard(view);
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
            KeyboardHelper.hideKeyboard(view);
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
            KeyboardHelper.hideKeyboard(view);
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

        searchCompetition = view.findViewById(R.id.search);
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
        addButton = view.findViewById(R.id.add_competition);
        addButton.setOnClickListener(v -> {
            startActivity(new Intent(this.getActivity().getBaseContext(), AddCompeActivity.class));
        });

        // Get competition list
        competitions = new ArrayList<>();
        competitionList = view.findViewById(R.id.competition_list);
        competitionList.setHasFixedSize(true);
        competitionListAdapter = new CompetitionListAdapter(competitions, isJoined, isCreated);
        competitionList.setAdapter(competitionListAdapter);
        layoutManager = new LinearLayoutManager(this.getActivity().getBaseContext());
        competitionList.setLayoutManager(layoutManager);

        if (isCreated) {
            fetchOwnCompetitionList(null, limit, offset);
        }
        else fetchCompetitionList(isJoined, null, limit, offset);

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

        return view;
    }

    private void initHeaderForRequest() {
        prefs = this.getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchCompetitionList(Boolean isJoined, String name, Integer limit, Integer offset) {

        Api.initGetCompetitionList(headers, isJoined, name, limit, offset);
        Api.getCompetitionList.clone().enqueue(new Callback<CompetitionListResponse>() {
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