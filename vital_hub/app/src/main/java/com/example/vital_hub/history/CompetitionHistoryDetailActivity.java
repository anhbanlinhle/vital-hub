package com.example.vital_hub.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.competition.data.CompetitionAllDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionHistoryDetailActivity extends AppCompatActivity {
    TextView competitionTitle;
    TextView competitionType;
    ImageView background;
    TextView hostName;
    TextView endDate;
    TextView participant;
    ImageView avatar1;
    ImageView avatar2;
    ImageView avatar3;
    TextView name1;
    TextView name2;
    TextView name3;
    TextView score1;
    TextView score2;
    TextView score3;
    ImageView backButton;
    Long competitionHistoryId;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    CompetitionAllDetail competitionAllDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competition_history_detail_layout);
        initHeaderForRequest();
        competitionTitle = findViewById(R.id.title);
        competitionType = findViewById(R.id.competition_type);
        background = findViewById(R.id.background);
        hostName = findViewById(R.id.host_name);
        endDate = findViewById(R.id.end_date);
        participant = findViewById(R.id.participant_count);
        avatar1 = findViewById(R.id.avatar_1st);
        avatar2 = findViewById(R.id.avatar_2nd);
        avatar3 = findViewById(R.id.avatar_3rd);
        name1 = findViewById(R.id.name_1st);
        name2 = findViewById(R.id.name_2nd);
        name3 = findViewById(R.id.name_3rd);
        score1 = findViewById(R.id.score_1st);
        score2 = findViewById(R.id.score_2nd);
        score3 = findViewById(R.id.score_3rd);
        backButton = findViewById(R.id.back_button);
        competitionHistoryId = getIntent().getLongExtra("competitionHistoryId", -1);
        if (competitionHistoryId == -1) {
            throw new RuntimeException("Invalid competition");
        }
        fetchData();

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
    private void fetchData() {
        Api.initGetCompetitionAllDetail(headers, competitionHistoryId);
        Api.competitionAllDetail.clone().enqueue(new Callback<CompetitionAllDetail>() {
            @Override
            public void onResponse(Call<CompetitionAllDetail> call, Response<CompetitionAllDetail> response) {
                if (response.isSuccessful()) {
                    competitionAllDetail = response.body();
                    if (competitionAllDetail != null) {
                        hostName.setText(competitionAllDetail.getDetail().getHost());
                        if ((competitionAllDetail.getDetail().getParticipants()).equals("1")) {
                            participant.setText(competitionAllDetail.getDetail().getParticipants() + " participant");
                        } else {
                            participant.setText(competitionAllDetail.getDetail().getParticipants() + " participants");
                        }
                        competitionTitle.setText(competitionAllDetail.getDetail().getTitle());
                        competitionType.setText(competitionAllDetail.getDetail().getType());
                        if (competitionAllDetail.getDetail().getBackground() != null) {
                            Glide.with(CompetitionHistoryDetailActivity.this).load(competitionAllDetail.getDetail().getBackground()).into(background);
                        }
                        if (competitionAllDetail.getRank().size() == 1) {
                            name1.setText(competitionAllDetail.getRank().get(0).getName());
                            score1.setText(competitionAllDetail.getRank().get(0).getScore());
                            if (competitionAllDetail.getRank().get(0).getAvatar() != null) {
                                Glide.with(CompetitionHistoryDetailActivity.this).load(competitionAllDetail.getRank().get(0).getAvatar()).into(avatar1);
                            }
                        }

                        if (competitionAllDetail.getRank().size() == 2) {
                            name1.setText(competitionAllDetail.getRank().get(0).getName());
                            score1.setText(competitionAllDetail.getRank().get(0).getScore());
                            if (competitionAllDetail.getRank().get(0).getAvatar() != null) {
                                Glide.with(CompetitionHistoryDetailActivity.this).load(competitionAllDetail.getRank().get(0).getAvatar()).into(avatar1);
                            }

                            name2.setText(competitionAllDetail.getRank().get(1).getName());
                            score2.setText(competitionAllDetail.getRank().get(1).getScore());
                            if (competitionAllDetail.getRank().get(1).getAvatar() != null) {
                                Glide.with(CompetitionHistoryDetailActivity.this).load(competitionAllDetail.getRank().get(1).getAvatar()).into(avatar2);
                            }
                        }

                        if (competitionAllDetail.getRank().size() == 3) {
                            name1.setText(competitionAllDetail.getRank().get(0).getName());
                            score1.setText(competitionAllDetail.getRank().get(0).getScore());
                            if (competitionAllDetail.getRank().get(0).getAvatar() != null) {
                                Glide.with(CompetitionHistoryDetailActivity.this).load(competitionAllDetail.getRank().get(0).getAvatar()).into(avatar1);
                            }

                            name2.setText(competitionAllDetail.getRank().get(1).getName());
                            score2.setText(competitionAllDetail.getRank().get(1).getScore());
                            if (competitionAllDetail.getRank().get(1).getAvatar() != null) {
                                Glide.with(CompetitionHistoryDetailActivity.this).load(competitionAllDetail.getRank().get(1).getAvatar()).into(avatar2);
                            }

                            name3.setText(competitionAllDetail.getRank().get(2).getName());
                            score3.setText(competitionAllDetail.getRank().get(2).getScore());
                            if (competitionAllDetail.getRank().get(2).getAvatar() != null) {
                                Glide.with(CompetitionHistoryDetailActivity.this).load(competitionAllDetail.getRank().get(2).getAvatar()).into(avatar3);
                            }
                        }
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date endDate1 = inputFormat.parse(competitionAllDetail.getDetail().getTime());
                            String formattedDate = outputFormat.format(endDate1);

                            endDate.setText(formattedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CompetitionAllDetail> call, Throwable t) {

            }
        });
    }


}
