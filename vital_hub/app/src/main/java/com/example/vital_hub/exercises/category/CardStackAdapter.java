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
        holder.description.setText(item.getDescription());
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
