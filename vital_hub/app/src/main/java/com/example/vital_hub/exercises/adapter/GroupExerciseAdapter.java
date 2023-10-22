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
import com.example.vital_hub.exercises.GroupExerciseActivity;
import com.example.vital_hub.exercises.data_container.GroupExercise;

import java.util.List;

public class GroupExerciseAdapter extends RecyclerView.Adapter<GroupExerciseAdapter.ViewHolder> {

    private List<GroupExercise> list;

    public GroupExerciseAdapter(List<GroupExercise> list) {
        this.list = list;
    }



    @NonNull
    @Override
    public GroupExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.group_exercise_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupExerciseAdapter.ViewHolder holder, int position) {
        GroupExercise groupExercise = list.get(position);

        holder.title.setText(groupExercise.getGroupName());
        holder.calo.setText(groupExercise.getTotalCaloStr());
        holder.exerciseCount.setText(groupExercise.getExerciseCountStr());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView calo;
        TextView exerciseCount;
        Button detailBtn;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.ge_title);
            calo = view.findViewById(R.id.ge_calo);
            exerciseCount = view.findViewById(R.id.ge_ex_count);
            detailBtn = view.findViewById(R.id.ge_btn_detail);

            buttonBinding();
        }

        private void buttonBinding() {
            detailBtn.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), GroupExerciseActivity.class);
                Long id = Long.parseLong(title.getText().toString().substring(6));
                intent.putExtra("group_id", id);
                v.getContext().startActivity(intent);
            });
        }

    }
}
