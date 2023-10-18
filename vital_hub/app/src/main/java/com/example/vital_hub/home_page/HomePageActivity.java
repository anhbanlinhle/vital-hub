package com.example.vital_hub.home_page;

import static com.example.vital_hub.LoginScreen.oneTapClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.LoginScreen;
import com.example.vital_hub.MainActivity;
import com.example.vital_hub.R;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    private RecyclerView hpRecycler;
    private ArrayList<HomePagePost> arrayList;
    boolean isLoading = false;
    HpRecyclerAdapter recyclerAdapter;

    ImageButton logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        arrayList = new ArrayList<>();

        hpRecycler = findViewById(R.id.home_page_recycler);

        logout_button = findViewById(R.id.logout_button);

        populateData(0);

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
                signOut();
            }
        });
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
}
