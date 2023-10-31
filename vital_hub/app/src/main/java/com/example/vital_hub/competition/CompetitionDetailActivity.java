package com.example.vital_hub.competition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;

public class CompetitionDetailActivity extends AppCompatActivity {

    private TextView compeTitle;
    private TextView compeType;
    private ImageView compeBackground;
    private TextView host;
    private TextView time;
    private TextView duration;
    private TextView participant;
    private ImageView avatar1;
    private ImageView avatar2;
    private ImageView avatar3;
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView score1;
    private TextView score2;
    private TextView score3;

    private ImageButton editBtn;
    private ImageButton deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);
        variableDeclaration();
    }

    private void variableDeclaration() {
        compeTitle = findViewById(R.id.detail_compe_title);
        compeType = findViewById(R.id.detail_compe_type);
        compeBackground = findViewById(R.id.compe_background);
        host = findViewById(R.id.compe_host);
        time = findViewById(R.id.compe_time);
        duration = findViewById(R.id.compe_duration);
        participant = findViewById(R.id.compe_participant);
        avatar1 = findViewById(R.id.avatar_1st);
        avatar2 = findViewById(R.id.avatar_2nd);
        avatar3 = findViewById(R.id.avatar_3rd);
        name1 = findViewById(R.id.name_1st);
        name2 = findViewById(R.id.name_2nd);
        name3 = findViewById(R.id.name_3rd);
        score1 = findViewById(R.id.score_1st);
        score2 = findViewById(R.id.score_2nd);
        score3 = findViewById(R.id.score_3rd);
        editBtn = findViewById(R.id.compe_btn_edit);
        deleteBtn = findViewById(R.id.compe_btn_delete);

        buttonBinding();

        Glide.with(CompetitionDetailActivity.this).load("https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s360-c-no").into(avatar1);
        Glide.with(CompetitionDetailActivity.this).load("https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s360-c-no").into(avatar2);
        Glide.with(CompetitionDetailActivity.this).load("https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s360-c-no").into(avatar3);

    }

    private void buttonBinding() {
        editBtn.setOnClickListener(v -> {
        });

        deleteBtn.setOnClickListener(v -> {

        });
    }
}