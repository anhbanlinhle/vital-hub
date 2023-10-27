package com.example.vital_hub.post_comment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.home_page.HomePagePost;
import com.example.vital_hub.home_page.HpRecyclerAdapter;

import java.util.ArrayList;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {
    private ArrayList<Comment> arrayList;
    final int VIEW_TYPE_COMMENT = 0;
    final int VIEW_TYPE_LOADING = 1;

    final int VIEW_TYPE_POST = 2;

    public CommentRecyclerAdapter(ArrayList<Comment> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_COMMENT) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.comment_layout, parent, false);
            return new ViewHolder(view, VIEW_TYPE_COMMENT);
        } else if (viewType == VIEW_TYPE_LOADING) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.loading_item, parent, false);
            return new ViewHolder(view, VIEW_TYPE_LOADING);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.hp_item_view, parent, false);
            return new ViewHolder(view, VIEW_TYPE_POST);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.ViewHolder holder, int position) {
        if (holder.itemType == VIEW_TYPE_COMMENT) {
            Comment cmt = arrayList.get(position);

            holder.avatar.setImageResource(cmt.getProfileIcon());
            holder.username.setText(cmt.getProfileName());
            holder.comment.setText(cmt.getContent());
        } else if (holder.itemType == VIEW_TYPE_POST) {
            HomePagePost post = arrayList.get(position).getPost();


            holder.isOwned = post.getOwned();
            holder.postId = post.getPostId();
            holder.userId = post.getUserId();
            holder.username.setText(post.getUsername());
            holder.comment.setText(post.getTitle());
            Glide.with(holder.avatar.getContext()).load(post.getAvatar()).into(holder.avatar);
            Glide.with(holder.postImage.getContext()).load(post.getAvatar()).into(holder.postImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return VIEW_TYPE_POST;
        } else return arrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Boolean isOwned;
        Long postId;
        Long userId;

        ImageView avatar;
        TextView username;
        TextView comment;

        ImageView postImage;

        int itemType;

        public ViewHolder(@NonNull View itemView, int viewType) {

            super(itemView);
            if (viewType == VIEW_TYPE_COMMENT) {
                avatar = itemView.findViewById(R.id.profile_image);
                username = itemView.findViewById(R.id.profile_name);
                comment = itemView.findViewById(R.id.comment_text);
                itemType = VIEW_TYPE_COMMENT;
            } else if (viewType == VIEW_TYPE_LOADING) {
                itemType = VIEW_TYPE_LOADING;
            } else {
                avatar = itemView.findViewById(R.id.post_profile_image);
                username = itemView.findViewById(R.id.post_profile_name);
                comment = itemView.findViewById(R.id.message);
                postImage = itemView.findViewById(R.id.post_image);
                itemType = VIEW_TYPE_POST;
            }
        }
    }
}
