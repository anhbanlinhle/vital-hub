package com.example.vital_hub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<HomePagePost> arrayList;
    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;

    public RecyclerAdapter(ArrayList<HomePagePost> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.hp_item_view, parent, false);
            return new ViewHolder(view, VIEW_TYPE_ITEM);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.loading_item, parent, false);
            return new ViewHolder(view, VIEW_TYPE_LOADING);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        if(holder.itemType == VIEW_TYPE_ITEM) {
            HomePagePost post = arrayList.get(position);

            holder.title.setText(post.getTitle());
            holder.message.setText(post.getMessage());
            holder.profileImage.setImageResource(post.getProfileIcon());
            holder.postImage.setImageResource(post.getPostImage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        ImageView postImage;
        TextView title;
        TextView message;

        int itemType;

        public ViewHolder(@NonNull View itemView, int viewType) {

            super(itemView);
            if (viewType == VIEW_TYPE_ITEM) {
                profileImage = itemView.findViewById(R.id.profile_image);
                postImage = itemView.findViewById(R.id.post_image);
                title = itemView.findViewById(R.id.title);
                message = itemView.findViewById(R.id.message);
                itemType = VIEW_TYPE_ITEM;
            } else {
                itemType = VIEW_TYPE_LOADING;
            }
        }
    }
}
