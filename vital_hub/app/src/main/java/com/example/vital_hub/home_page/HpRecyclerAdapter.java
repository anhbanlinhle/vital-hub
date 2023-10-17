package com.example.vital_hub.home_page;


import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vital_hub.R;
import com.example.vital_hub.post_comment.PostCommentActivity;

import java.util.ArrayList;

public class HpRecyclerAdapter extends RecyclerView.Adapter<HpRecyclerAdapter.ViewHolder> {

    private ArrayList<HomePagePost> arrayList;
    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;

    public HpRecyclerAdapter(ArrayList<HomePagePost> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HpRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull HpRecyclerAdapter.ViewHolder holder, int position) {
        if (holder.itemType == VIEW_TYPE_ITEM) {
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
        LottieAnimationView lottieHeart;
        ImageButton comment_button;
        boolean switchOn = false;

        int itemType;

        public ViewHolder(@NonNull View itemView, int viewType) {

            super(itemView);
            if (viewType == VIEW_TYPE_ITEM) {
                profileImage = itemView.findViewById(R.id.post_profile_image);
                postImage = itemView.findViewById(R.id.post_image);
                title = itemView.findViewById(R.id.post_profile_name);
                message = itemView.findViewById(R.id.message);
                lottieHeart = itemView.findViewById(R.id.like_button);
                itemType = VIEW_TYPE_ITEM;
                comment_button = itemView.findViewById(R.id.cmt_button);

                lottieHeart.setProgress(0.0f);


                lottieHeart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (switchOn) {
                            lottieHeart.setMinAndMaxProgress(0.9f, 1.0f);
                            lottieHeart.playAnimation();
                            switchOn = false;
                        } else {
                            lottieHeart.setMinAndMaxProgress(0.4f, 0.8f);
                            lottieHeart.playAnimation();
                            switchOn = true;
                        }
                    }
                });

                comment_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), PostCommentActivity.class);
                        view.getContext().startActivity(intent);
                    }
                });
            } else {
                itemType = VIEW_TYPE_LOADING;
            }
        }
    }
}
