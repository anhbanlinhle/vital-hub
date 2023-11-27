package com.example.vital_hub.exercises.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder>{
    private List<Items> items;

    public CardStackAdapter(List<Items> items) {
        this.items = items;
    }

    @Override
    public CardStackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_exercise_item, parent, false);
        return new CardStackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardStackAdapter.ViewHolder holder, int position) {
        Items item = items.get(position);
        holder.name.setText(item.getName());
//        if (item.getDescription() == "Gym") {
//            holder.description.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exercise_weight_lift, 0, 0, 0);
//        }
//        else if (item.getDescription() == "Running") {
//            holder.description.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_directions_run_24_white, 0, 0, 0);
//        }
//        else if (item.getDescription() == "Bicycle") {
//            holder.description.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_directions_bike_24_white, 0, 0, 0);
//        }
//        else if (item.getDescription() == "Pushup") {
//            holder.description.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pushup_pose, 0, 0, 0);
//        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public List<Items> getItems() {
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            description = view.findViewById(R.id.item_description);
        }
    }
}
