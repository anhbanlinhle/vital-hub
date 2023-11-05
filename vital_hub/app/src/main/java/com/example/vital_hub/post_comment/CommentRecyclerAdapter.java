package com.example.vital_hub.post_comment;

import android.content.Intent;
import android.media.Image;
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
import com.example.vital_hub.home_page.HomePagePost;
import com.example.vital_hub.home_page.HpRecyclerAdapter;
import java.util.ArrayList;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
            holder.isOwned = cmt.getOwned();
            holder.profileName.setText(cmt.getProfileName());
            Glide.with(holder.profileImage.getContext()).load(cmt.getAvatar()).into(holder.profileImage);
            holder.comment_text.setText(cmt.getContent());
        } else if (holder.itemType == VIEW_TYPE_POST) {
            HomePagePost post = arrayList.get(position).getPost();
            holder.isOwned = post.getOwned();
            holder.post_profile_name.setText(post.getUsername());
            holder.post_text.setText(post.getTitle());
            Glide.with(holder.post_profile_image.getContext()).load(post.getAvatar()).into(holder.post_profile_image);
            Glide.with(holder.post_image.getContext()).load(post.getImage()).into(holder.post_image);
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

    public void deleteComment(int position) {
        arrayList.remove(position);
        notifyItemRangeChanged(position, 1);
    }
}
