package com.example.vital_hub.home_page;


import static com.example.vital_hub.authentication.LoginScreen.oneTapClient;

import com.example.vital_hub.authentication.LoginScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.client.controller.Api;
import com.example.vital_hub.client.objects.PostResponse;
import com.example.vital_hub.client.objects.PostResponse;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.ExerciseActivity;
import com.example.vital_hub.R;
import com.example.vital_hub.UserProfile;
import com.example.vital_hub.exercises.ChooseExerciseActivity;
import com.example.vital_hub.model.Friend;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private RecyclerView hpRecycler;
    private ArrayList<HomePagePost> arrayList;
    boolean isLoading = false;
    HpRecyclerAdapter recyclerAdapter;

    ImageButton logout_button;
    BottomNavigationView bottomNavigationView;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    public static PostResponse postResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        initHeaderForRequest();
        arrayList = new ArrayList<>();

        hpRecycler = findViewById(R.id.home_page_recycler);

        logout_button = findViewById(R.id.logout_button);

        populateData(0);

        //NavBar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        recyclerAdapter = new HpRecyclerAdapter(arrayList);
        hpRecycler.setAdapter(recyclerAdapter);
        hpRecycler.setLayoutManager(new LinearLayoutManager(this));

        hpRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) hpRecycler.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size() - 1) {
                        isLoading = true;
                        getMoreData();
                    }
                }
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click", "fetch");
                fetchPost(10);
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
            return true;
        } else if (item.getItemId() == R.id.exercise) {
            startActivity(new Intent(getApplicationContext(), ChooseExerciseActivity.class));
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

    private void getMoreData() {
        // ADD DATA FROM DB
        arrayList.remove(arrayList.size() - 1);
        populateData(arrayList.size());
        recyclerAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    private void signOut() {
        oneTapClient.signOut();
        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(HomePageActivity.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    private void populateData(int currentSize) {
        currentSize++;
        int nextSize = currentSize + 10;
        for (; currentSize < nextSize; currentSize++) {
            arrayList.add(new HomePagePost(R.drawable.ic_launcher_background, R.drawable.app_icon, "title", String.valueOf(currentSize)));
        }
        arrayList.add(null);
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchPost(int pageNum) {
        Api.initGetPostResponse(headers, pageNum);
        Api.getPostResponse.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    postResponse = response.body();
                    for(HomePagePost post : postResponse.getData()) {
                        Log.d("success", post.getId().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}
