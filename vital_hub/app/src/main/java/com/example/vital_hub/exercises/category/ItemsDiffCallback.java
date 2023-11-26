package com.example.vital_hub.exercises.category;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class ItemsDiffCallback  extends DiffUtil.Callback {
    private List<Items> oldList;
    private List<Items> newList;

    public ItemsDiffCallback(List<Items> oldList, List<Items> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Items oldItem = oldList.get(oldItemPosition);
        Items newItem = newList.get(newItemPosition);

        return oldItem.getName().equals(newItem.getName()) &&
                oldItem.getDescription().equals(newItem.getDescription());
    }
}
