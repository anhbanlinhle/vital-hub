package com.example.vital_hub.exercises;

import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


import com.example.vital_hub.R;
import com.example.vital_hub.bicycle.BicycleTrackerActivity;
import com.example.vital_hub.exercises.category.CardStackAdapter;
import com.example.vital_hub.exercises.category.Items;
import com.example.vital_hub.exercises.category.ItemsDiffCallback;
import com.example.vital_hub.home_page.AddPostActivity;
import com.example.vital_hub.pushup.PushupVideoScan;
import com.example.vital_hub.running.RunningActivity;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class ExerciseGeneralFragment extends Fragment {
    ImageView runActivity;
    ImageView bikeActivity;
    ImageView gymActivity;
    ImageView pushupActivity;

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
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_exercise, container, false);
        runActivity = view.findViewById(R.id.run_redirect);
        bikeActivity = view.findViewById(R.id.bike_redirect);
        gymActivity = view.findViewById(R.id.gym_redirect);
        pushupActivity = view.findViewById(R.id.pushup_redirect);


        runActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), RunningActivity.class);
                startActivity(intent);
            }
        });

        bikeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), BicycleTrackerActivity.class);
                startActivity(intent);
            }
        });

        gymActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChooseExerciseActivity.class);
                startActivity(intent);
            }
        });

        pushupActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), PushupVideoScan.class);
                startActivity(intent);
            }
        });
        return view;
    }
}