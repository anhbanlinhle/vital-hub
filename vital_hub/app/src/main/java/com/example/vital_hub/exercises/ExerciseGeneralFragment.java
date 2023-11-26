package com.example.vital_hub.exercises;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.vital_hub.R;
import com.example.vital_hub.bicycle.BicycleTrackerActivity;
import com.example.vital_hub.pushup.PushupVideoScan;
import com.example.vital_hub.running.RunningActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseGeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseGeneralFragment extends Fragment {
    private ImageButton chooseGymBtn;
    private ImageButton chooseRunBtn;
    private ImageButton chooseBicycleBtn;
    private ImageButton choosePushupBtn;
    public ExerciseGeneralFragment() {
        // Required empty public constructor
    }

    public static ExerciseGeneralFragment newInstance() {
        ExerciseGeneralFragment fragment = new ExerciseGeneralFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void btnBinding() {
        chooseGymBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity().getBaseContext(), ChooseExerciseActivity.class));
        });
        choosePushupBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity().getBaseContext(), PushupVideoScan.class));
        });
        chooseBicycleBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity().getBaseContext(), BicycleTrackerActivity.class));
        });
        chooseRunBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity().getBaseContext(), RunningActivity.class));
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_exercise, container, false);
        chooseGymBtn = view.findViewById(R.id.choose_gym_btn);
        chooseRunBtn = view.findViewById(R.id.choose_run_btn);
        choosePushupBtn = view.findViewById(R.id.choose_pushup_btn);
        chooseBicycleBtn = view.findViewById(R.id.choose_bicycle_btn);

        btnBinding();
        return view;
    }
}