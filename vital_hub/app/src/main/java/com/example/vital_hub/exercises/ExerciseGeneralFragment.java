package com.example.vital_hub.exercises;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vital_hub.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseGeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseGeneralFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_exercise, container, false);
    }
}