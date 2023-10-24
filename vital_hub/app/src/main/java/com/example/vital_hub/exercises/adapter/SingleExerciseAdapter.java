package com.example.vital_hub.exercises.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.exercises.SingleExerciseActivity;
import com.example.vital_hub.exercises.data_container.SingleExercise;

import java.util.List;

public class SingleExerciseAdapter extends RecyclerView.Adapter<SingleExerciseAdapter.ViewHolder> {

    private List<SingleExercise> list;

    final int VIEW_TYPE_ITEM = 0;
    int VIEW_TYPE_LOADING = 1;

    public SingleExerciseAdapter(List<SingleExercise> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SingleExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.single_exercise_layout, parent, false);
            return new SingleExerciseAdapter.ViewHolder(view, VIEW_TYPE_ITEM);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.loading_item, parent, false);
            return new SingleExerciseAdapter.ViewHolder(view, VIEW_TYPE_LOADING);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull SingleExerciseAdapter.ViewHolder holder, int position) {
        if (holder.itemType == VIEW_TYPE_ITEM) {
            SingleExercise singleExercise = list.get(position);
            holder.name.setText(singleExercise.getName());
            holder.title.setText(singleExercise.getTitle());
            holder.repSet.setText(singleExercise.getRepSet());
            holder.calo.setText(singleExercise.getCaloriesString());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView calo;

        TextView name;

        TextView repSet;
        Button detailBtn;

        int itemType;

        public ViewHolder(View view, int viewType) {
            super(view);

            if (viewType == VIEW_TYPE_ITEM) {
                itemType = VIEW_TYPE_ITEM;
                title = view.findViewById(R.id.se_title);
                calo = view.findViewById(R.id.se_calories);
                name = view.findViewById(R.id.se_name);
                repSet = view.findViewById(R.id.se_reps_set);
                detailBtn = view.findViewById(R.id.se_btn_detail);

                buttonBinding();
            } else {
                itemType = VIEW_TYPE_LOADING;
            }

        }

        private void buttonBinding() {
            detailBtn.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SingleExerciseActivity.class);
                Long id = Long.parseLong(title.getText().toString().substring(1));
                intent.putExtra("exercise_id", id);
                v.getContext().startActivity(intent);
            });
        }

    }
}
