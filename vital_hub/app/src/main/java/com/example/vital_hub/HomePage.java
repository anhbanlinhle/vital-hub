package com.example.vital_hub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private RecyclerView hpRecycler;
    private ArrayList<HomePagePost> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        arrayList = new ArrayList<>();

        hpRecycler = findViewById(R.id.home_page_recycler);

        arrayList.add(new HomePagePost(R.drawable.ic_launcher_background, R.drawable.app_icon, "title", "message"));
        arrayList.add(new HomePagePost(R.drawable.ic_launcher_background, R.drawable.app_icon, "title", "message"));
        arrayList.add(new HomePagePost(R.drawable.ic_launcher_background, R.drawable.app_icon, "title", "message"));
        arrayList.add(new HomePagePost(R.drawable.ic_launcher_background, R.drawable.app_icon, "title", "message"));

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(arrayList);

        hpRecycler.setAdapter(recyclerAdapter);
        hpRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
