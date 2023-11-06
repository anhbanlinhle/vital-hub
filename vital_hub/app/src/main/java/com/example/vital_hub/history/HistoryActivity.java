package com.example.vital_hub.history;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.competition.CompetitionListAdapter;
import com.example.vital_hub.helper.KeyboardHelper;

import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private int limit = 10;
    private int offset = 0;
    private RecyclerView competitionHistoryList;
    private CompetitionListAdapter competitionHistoryListAdapter;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        initHeaderForRequest();
        KeyboardHelper.setupKeyboardHiding(this);
    }
    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }



}
