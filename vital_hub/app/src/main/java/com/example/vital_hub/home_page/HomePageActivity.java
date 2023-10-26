package com.example.vital_hub.home_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.R;
import com.example.vital_hub.UserProfile;
import com.example.vital_hub.exercises.ChooseExerciseActivity;
import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private RecyclerView hpRecycler;
    private ArrayList<HomePagePost> arrayList;
    boolean isLoading = false;
    HpRecyclerAdapter recyclerAdapter;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        arrayList = new ArrayList<>();

        hpRecycler = findViewById(R.id.home_page_recycler);

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
        populateData(arrayList.size());
        recyclerAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    private void populateData(int currentSize) {
        currentSize++;
        int nextSize = currentSize + 10;
        for (; currentSize < nextSize; currentSize++) {
            arrayList.add(new HomePagePost(R.drawable.ic_launcher_background, R.drawable.app_icon, "title", String.valueOf(currentSize)));
        }
        arrayList.add(null);
    }
}
