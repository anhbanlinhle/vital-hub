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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.controller.Api;
import com.example.vital_hub.exercises.adapter.SingleExerciseAdapter;
import com.example.vital_hub.exercises.data_container.SingleExercise;
import com.example.vital_hub.helper.EndlessScrollListener;
import com.example.vital_hub.utils.HeaderInitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private final String[] sortOptions = {"Id" ,"Name", "Calories"};

    private Spinner spinner;

    private SwitchCompat descSwitch;

    private RecyclerView seRecycler;

    private SingleExerciseAdapter singleExerciseAdapter;

    private List<SingleExercise> list;

    private Integer pageIndex;

    private Integer pageSize;

    private Map<String, String> header;

    private String sort;

    private Boolean desc;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    public static ExerciseFragment newInstance(String param1, String param2) {
        ExerciseFragment fragment = new ExerciseFragment();
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
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        variableInit();
    }

    private void variableInit() {
        pageIndex = 0;
        pageSize = 10;
        header = HeaderInitUtil.headerWithToken(getContext());
        desc = false;

        descSwitch = (SwitchCompat) getView().findViewById(R.id.exercise_sort_desc_switch);
        spinner = (Spinner) getView().findViewById(R.id.exercise_sort_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        list = new ArrayList<>();
        singleExerciseAdapter = new SingleExerciseAdapter(list);
        seRecycler = (RecyclerView) getView().findViewById(R.id.se_recycler);
        seRecycler.setAdapter(singleExerciseAdapter);
        seRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        listenerBinding();
    }

    private void listenerBinding() {

        descSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                desc = b;
                resetList();
            }
        });
        seRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) seRecycler.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size() - 1) {
                    pageIndex++;
                    fetchExerciseData(false);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sortOptions[i].equals(sortOptions[2])) {
                    sort = "totalCalo";
                } else {
                    sort = sortOptions[i].toLowerCase();
                }
                resetList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fetchExerciseData(boolean isReset) {
        Api.getListSingleExerciseByPage(header, pageIndex, pageSize, sort == null ? "id" : sort, desc);

        Api.singleExerciseList.clone().enqueue(new Callback<List<SingleExercise>>() {
            @Override
            public void onResponse(Call<List<SingleExercise>> call, Response<List<SingleExercise>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        list.addAll(response.body());
                        if (isReset) {
                            singleExerciseAdapter.notifyDataSetChanged();
                        } else {
                            singleExerciseAdapter.notifyItemRangeChanged(list.size() - pageSize, pageSize);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SingleExercise>> call, Throwable t) {
                Toast.makeText(getContext(), "Fail to get exercises", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetList() {
        pageIndex = 0;
        list.clear();
        fetchExerciseData(true);
    }
}