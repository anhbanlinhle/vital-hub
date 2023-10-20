package com.example.vital_hub.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.vital_hub.R;
import com.example.vital_hub.exercises.adapter.GroupExerciseAdapter;
import com.example.vital_hub.exercises.data_container.GroupExercise;

import java.util.ArrayList;
import java.util.List;

public class GroupExerciseActivity extends AppCompatActivity {

    private RecyclerView geRecycler;
    private GroupExerciseAdapter groupExerciseAdapter;
    private List<GroupExercise> geList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_exercise);


    }

    private void dataInit() {
        geList = new ArrayList<>();
        geRecycler = (RecyclerView) findViewById(R.id.ge_recycler);
    }
}