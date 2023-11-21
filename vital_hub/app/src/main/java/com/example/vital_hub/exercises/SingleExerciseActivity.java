package com.example.vital_hub.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.exercises.data_container.SingleExercise;
import com.example.vital_hub.utils.HeaderInitUtil;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleExerciseActivity extends AppCompatActivity {

    private TextView title;
    private TextView description;
    private TextView repSets;
    private TextView calo;
    private TextView back;

    private Long id;

    private Map<String, String> header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_exercise);

        initData();
    }

    private void initData() {
        header = HeaderInitUtil.headerWithToken(this);
        id = getIntent().getLongExtra("exercise_id", 0);
        back = (TextView) findViewById(R.id.back_to_choose_ex);
        title = (TextView) findViewById(R.id.se_ac_title);
        description = (TextView) findViewById(R.id.se_ac_description);
        repSets = (TextView) findViewById(R.id.se_ac_reps_set);
        calo = (TextView) findViewById(R.id.se_ac_calories);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchData();
    }

    private void fetchData() {
        Api.getSingleExerciseById(header, id);
        Api.singleExercise.clone().enqueue(new Callback<SingleExercise>() {
            @Override
            public void onResponse(Call<SingleExercise> call, Response<SingleExercise> response) {
                if (response.isSuccessful()) {
                    SingleExercise singleExercise = response.body();
                    title.setText(singleExercise.getName());
                    description.setText(singleExercise.getDescription());
                    repSets.setText(singleExercise.getRepSet());
                    calo.setText(singleExercise.getCaloriesString());
                }
            }

            @Override
            public void onFailure(Call<SingleExercise> call, Throwable t) {
                Toast.makeText(SingleExerciseActivity.this, "Fail to get exercise " + id, Toast.LENGTH_SHORT).show();
            }
        });
    }
}