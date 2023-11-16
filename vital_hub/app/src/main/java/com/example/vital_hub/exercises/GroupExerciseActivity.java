package com.example.vital_hub.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.exercises.adapter.SingleExerciseAdapter;
import com.example.vital_hub.exercises.data_container.SingleExercise;
import com.example.vital_hub.utils.HeaderInitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupExerciseActivity extends AppCompatActivity {

    private RecyclerView geRecycler;
    private SingleExerciseAdapter singleExerciseAdapter;
    private List<SingleExercise> seList;

    private Long groupId;

    private Map<String, String> header;

    private TextView title;
    private TextView back;

    private ImageButton submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_exercise);

        dataInit();
    }

    private void dataInit() {
        seList = new ArrayList<>();
        groupId = getIntent().getLongExtra("group_id", 0);
        back = (TextView) findViewById(R.id.back_to_choose_ex);
        title = (TextView) findViewById(R.id.ge_ac_title);
        title.setText("Group " + groupId);
        submitBtn = findViewById(R.id.submit_ex_btn);
        header = HeaderInitUtil.headerWithToken(this);
        geRecycler = (RecyclerView) findViewById(R.id.ge_ac_recycler);

        buttonBinding();

        fetchExerciseInGroup();
    }

    private void buttonBinding() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submitBtn.setOnClickListener(v -> {
            Toast.makeText(this, "submit", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchExerciseInGroup() {
        Api.getGroupExerciseById(header, groupId);

        Api.exercisesInGroup.clone().enqueue(new Callback<List<SingleExercise>>() {
            @Override
            public void onResponse(Call<List<SingleExercise>> call, Response<List<SingleExercise>> response) {
                if (response.isSuccessful()) {
                    seList = response.body();

                    singleExerciseAdapter = new SingleExerciseAdapter(seList);
                    geRecycler.setAdapter(singleExerciseAdapter);
                    geRecycler.setLayoutManager(new LinearLayoutManager(GroupExerciseActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<SingleExercise>> call, Throwable t) {
                Toast.makeText(GroupExerciseActivity.this, "Fail to get exercise in group " + groupId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}