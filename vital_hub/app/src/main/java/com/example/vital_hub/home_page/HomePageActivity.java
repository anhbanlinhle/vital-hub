package com.example.vital_hub.home_page;


import static com.example.vital_hub.authentication.LoginScreen.oneTapClient;
import static com.example.vital_hub.client.spring.controller.Api.initGetPostResponse;

import com.example.vital_hub.SplashScreen;
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

import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.R;
import com.example.vital_hub.post_comment.PostCommentActivity;
import com.example.vital_hub.profile.UserProfile;
import com.example.vital_hub.exercises.ChooseExerciseActivity;
import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.example.vital_hub.friend.Friend;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static List<HomePagePost> postResponse;
    int pageNum = 0;

    FloatingActionButton addPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        initHeaderForRequest();
        arrayList = new ArrayList<>();

        hpRecycler = findViewById(R.id.home_page_recycler);

        logout_button = findViewById(R.id.logout_button);
        addPostButton = findViewById(R.id.add_post_button);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        recyclerAdapter = new HpRecyclerAdapter(arrayList);
        hpRecycler.setAdapter(recyclerAdapter);
        hpRecycler.setLayoutManager(new LinearLayoutManager(this));

        populateData(0);

        //NavBar


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
                fetchPost(0);
            }
        });

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, AddPostActivity.class);
                startActivity(intent);
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

    private void getMoreData() {
        // ADD DATA FROM DB
        arrayList.remove(arrayList.size() - 1);
        fetchPost(pageNum);
        pageNum++;
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
        fetchPost(pageNum);
        pageNum++;
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchPost(int pageNum) {
        initGetPostResponse(headers, pageNum);
        Api.getPostResponse.clone().enqueue(new Callback<List<HomePagePost>>() {
            @Override
            public void onResponse(Call<List<HomePagePost>> call, Response<List<HomePagePost>> response) {
                if (response.isSuccessful()) {
                    postResponse = response.body();
                    for(HomePagePost post : postResponse) {
                        post.setTitle(String.valueOf(post.getPostId()));
                        arrayList.add(post);
                    }
                    recyclerAdapter.notifyItemRangeChanged(arrayList.size() - postResponse.size(), postResponse.size());
                    arrayList.add(null);
                }
            }

            @Override
            public void onFailure(Call<List<HomePagePost>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}
