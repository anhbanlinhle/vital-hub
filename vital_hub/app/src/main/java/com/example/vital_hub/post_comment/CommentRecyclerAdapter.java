package com.example.vital_hub.post_comment;

import static android.content.Context.MODE_PRIVATE;

import static com.example.vital_hub.client.spring.controller.Api.initDeleteComment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.home_page.HomePagePost;
import com.example.vital_hub.home_page.HpRecyclerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {
    private ArrayList<Comment> arrayList;
    final int VIEW_TYPE_COMMENT = 0;
    final int VIEW_TYPE_LOADING = 1;
    final int VIEW_TYPE_POST = 2;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;

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
            holder.isOwned = cmt.getOwned();
            holder.profileName.setText(cmt.getProfileName());
            Glide.with(holder.profileImage.getContext()).load(cmt.getAvatar()).into(holder.profileImage);
            holder.comment_text.setText(cmt.getContent());
        } else if (holder.itemType == VIEW_TYPE_POST) {
            HomePagePost post = arrayList.get(position).getPost();
            holder.isOwned = post.getOwned();
            holder.post_profile_name.setText(post.getUsername());
            holder.post_text.setText(post.getTitle());
            holder.post_score.setText(post.getScore());
            holder.post_calo.setText(post.getCalo().toString());
            Glide.with(holder.post_profile_image.getContext()).load(post.getAvatar()).into(holder.post_profile_image);
            Glide.with(holder.post_image.getContext()).load(post.getImage()).into(holder.post_image);

            switch (post.getType()) {
                case "RUNNING": {
                    holder.post_ex_icon.setImageResource(R.drawable.running_ex);
                    break;
                } case "GYM": {
                    holder.post_ex_icon.setImageResource(R.drawable.gym_ex);
                    break;
                } case "PUSHUP": {
                    holder.post_ex_icon.setImageResource(R.drawable.pushup_ex);
                    break;
                } case "BICYCLING": {
                    holder.post_ex_icon.setImageResource(R.drawable.bike_ex);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(arrayList.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else if (arrayList.get(position).getPost() != null) {
            return VIEW_TYPE_POST;
        } else {
            return VIEW_TYPE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        //comment

        TextView profileName;
        ImageView profileImage;
        TextView comment_text;
        Boolean isOwned;
        LinearLayout comment_body;

        // post
        ImageView post_profile_image;
        ImageView post_image;
        TextView post_profile_name;
        TextView post_text;
        ImageButton post_kebab_button;
        ImageView post_ex_icon;
        TextView post_score;
        TextView post_calo;
        int itemType;

        public ViewHolder(@NonNull View itemView, int viewType) {

            super(itemView);
            if (viewType == VIEW_TYPE_COMMENT) {
                profileName = itemView.findViewById(R.id.profile_name);
                profileImage = itemView.findViewById(R.id.profile_image);
                comment_text = itemView.findViewById(R.id.comment_text);
                comment_body = itemView.findViewById(R.id.comment_body);
                comment_body.setOnCreateContextMenuListener(this);
                itemType = VIEW_TYPE_COMMENT;
            } else if (viewType == VIEW_TYPE_LOADING) {
                itemType = VIEW_TYPE_LOADING;
            } else {
                itemType = VIEW_TYPE_POST;
                post_profile_image = itemView.findViewById(R.id.post_profile_image);
                post_image = itemView.findViewById(R.id.post_image);
                post_profile_name = itemView.findViewById(R.id.post_profile_name);
                post_text = itemView.findViewById(R.id.message);
                post_kebab_button = itemView.findViewById(R.id.kebab_button);
                post_ex_icon = itemView.findViewById(R.id.post_ex_icon);
                post_score = itemView.findViewById(R.id.post_score);
                post_calo = itemView.findViewById(R.id.post_calo);

                post_kebab_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Do you want to delete this comment ?");
            contextMenu.add(getAdapterPosition(), 101, 0, "Yes");
            contextMenu.add(getAdapterPosition(), 102, 1, "No");
        }
    }

    public void deleteComment(int position, Context context) {
        if(arrayList.get(position).getOwned()) {
            initHeaderForRequest(context);
            initDeleteComment(headers, arrayList.get(position).getId());
            Api.deleteComment.clone().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Fail", "Error" + response);
                        return;
                    }
                    arrayList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Delete comment successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Fail to delete comment", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Cannot delete comment that is not yours", Toast.LENGTH_SHORT).show();
        }
    }

    private void initHeaderForRequest(Context context) {
        prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }
}
