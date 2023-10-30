package com.example.vital_hub.exercises.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.exercises.adapter.GroupExerciseAdapter;
import com.example.vital_hub.exercises.data_container.GroupExercise;
import com.example.vital_hub.utils.HeaderInitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView geRecycler;
    private GroupExerciseAdapter groupExerciseAdapter;
    private List<GroupExercise> geList;

    private SwitchCompat suggestSwitch;

    private Map<String, String> header;

    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        variableInit();
    }

    private void variableInit() {
        geList = new ArrayList<>();
        geRecycler = (RecyclerView) getView().findViewById(R.id.ge_recycler);
        header = HeaderInitUtil.headerWithToken(getContext());
        suggestSwitch = (SwitchCompat) getView().findViewById(R.id.switch_ex_suggestion);
        suggestSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                fetchGroupExData(b);
            }
        });

        fetchGroupExData(false);

    }

    private void fetchGroupExData(boolean isSuggest) {
        Api.getListGroupExercise(header, isSuggest);
        Api.groupExerciseList.clone().enqueue(new Callback<List<GroupExercise>>() {
            @Override
            public void onResponse(Call<List<GroupExercise>> call, Response<List<GroupExercise>> response) {
                if (response.isSuccessful()) {
                    geList = response.body();

                    groupExerciseAdapter = new GroupExerciseAdapter(geList);
                    geRecycler.setAdapter(groupExerciseAdapter);
                    geRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onFailure(Call<List<GroupExercise>> call, Throwable t) {
                Toast.makeText(getContext(), "Fail to get exercise", Toast.LENGTH_SHORT).show();
            }
        });
    }
}