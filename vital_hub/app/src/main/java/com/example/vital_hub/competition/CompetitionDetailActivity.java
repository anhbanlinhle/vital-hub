package com.example.vital_hub.competition;

import static com.example.vital_hub.client.spring.controller.Api.initRetrofitAndController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.competition.data.CompetitionAllDetail;
import com.example.vital_hub.exercises.adapter.GroupExerciseAdapter;
import com.example.vital_hub.utils.HeaderInitUtil;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    SharedPreferences prefs;

    private Map<String, String> header;

    private CompetitionAllDetail competitionAllDetail;

    private Boolean isOwned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);
        variableDeclaration();
    }

    private void variableDeclaration() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        initRetrofitAndController(prefs.getString("server", "10.0.2.2"));
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
        header = HeaderInitUtil.headerWithToken(this);

        buttonBinding();

        fetchData();
    }

    private void buttonBinding() {
        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CompetitionDetailActivity.this, EditCompetitionActivity.class);
            intent.putExtra("id", competitionAllDetail.getDetail().getId());
            intent.putExtra("title", compeTitle.getText());
            String[] times = time.getText().toString().split(" - ");
            intent.putExtra("startedAt", times[0]);
            intent.putExtra("endedAt", times[1]);
            intent.putExtra("duration", competitionAllDetail.getDetail().getDuration());
            startActivity(intent);
        });

        deleteBtn.setOnClickListener(v -> {
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchData() {
        Api.initGetCompetitionAllDetail(header, 1L);
        Api.competitionAllDetail.clone().enqueue(new Callback<CompetitionAllDetail>() {
            @Override
            public void onResponse(Call<CompetitionAllDetail> call, Response<CompetitionAllDetail> response) {
                if (response.isSuccessful()) {
                    competitionAllDetail = response.body();
                    if (competitionAllDetail != null) {
                        bindData(competitionAllDetail);
                    }
                }
            }

            @Override
            public void onFailure(Call<CompetitionAllDetail> call, Throwable t) {

            }
        });
    }

    private void bindData(CompetitionAllDetail competitionAllDetail) {
        duration.setText(competitionAllDetail.getDetail().getDuration());
        host.setText(competitionAllDetail.getDetail().getHost());
        participant.setText(competitionAllDetail.getDetail().getParticipants() + " participants");
        time.setText(competitionAllDetail.getDetail().getTime());
        compeTitle.setText(competitionAllDetail.getDetail().getTitle());
        compeType.setText(competitionAllDetail.getDetail().getType());
        if (competitionAllDetail.getDetail().getBackground() != null) {
            Glide.with(CompetitionDetailActivity.this).load(competitionAllDetail.getDetail().getBackground()).into(compeBackground);
        }

        name1.setText(competitionAllDetail.getRank().get(0).getName());
        name2.setText(competitionAllDetail.getRank().get(1).getName());
        name3.setText(competitionAllDetail.getRank().get(2).getName());
        score1.setText(competitionAllDetail.getRank().get(0).getScore());
        score2.setText(competitionAllDetail.getRank().get(1).getScore());
        score3.setText(competitionAllDetail.getRank().get(2).getScore());
        if (competitionAllDetail.getRank().get(0).getAvatar() != null) {
            Glide.with(CompetitionDetailActivity.this).load(competitionAllDetail.getRank().get(0).getAvatar()).into(avatar1);
        }
        if (competitionAllDetail.getRank().get(1).getAvatar() != null) {
            Glide.with(CompetitionDetailActivity.this).load(competitionAllDetail.getRank().get(1).getAvatar()).into(avatar2);
        }
        if (competitionAllDetail.getRank().get(2).getAvatar() != null) {
            Glide.with(CompetitionDetailActivity.this).load(competitionAllDetail.getRank().get(2).getAvatar()).into(avatar3);
        }

        isOwned = competitionAllDetail.getIsOwned();

//        if (!isOwned) {
//            editBtn.setVisibility(View.GONE);
//        }
    }
}