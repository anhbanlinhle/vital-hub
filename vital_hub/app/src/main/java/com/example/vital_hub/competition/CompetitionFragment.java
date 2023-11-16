package com.example.vital_hub.competition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vital_hub.R;

public class CompetitionFragment extends Fragment {

    public CompetitionFragment() {
        // Required empty public constructor
    }

    public static CompetitionFragment newInstance(String param1, String param2) {
        CompetitionFragment fragment = new CompetitionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_competition, container, false);
        return view;
    }
}