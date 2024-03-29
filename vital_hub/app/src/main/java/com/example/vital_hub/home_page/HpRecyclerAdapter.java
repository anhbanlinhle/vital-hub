package com.example.vital_hub.home_page;


import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.post_comment.PostCommentActivity;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.vital_hub.client.spring.controller.Api.initDeletePost;

import static java.security.AccessController.getContext;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            if (!post.getOwned()) {
                holder.kebab_button.setVisibility(View.GONE);
            } else {
                holder.kebab_button.setVisibility(View.VISIBLE);
            }

            holder.type = post.getType();
            holder.isOwned = post.getOwned();
            holder.postId = post.getPostId();
            holder.userId = post.getUserId();
            holder.title.setText(post.getUsername());
            holder.message.setText(post.getTitle());
            holder.post_score.setText(post.getScore());
            holder.post_calo.setText(String.format("%1$,.2f", post.getCalo()) + " calo");
            Glide.with(holder.profileImage.getContext()).load(post.getAvatar()).into(holder.profileImage);
            Glide.with(holder.postImage.getContext()).load(post.getImage()).into(holder.postImage);

            switch (post.getType()) {
                case "RUNNING": {
                    holder.post_ex_icon.setImageResource(R.drawable.running_ex);
                    break;
                }
                case "GYM": {
                    holder.post_ex_icon.setImageResource(R.drawable.gym_ex);
                    break;
                }
                case "PUSHUP": {
                    holder.post_ex_icon.setImageResource(R.drawable.pushup_ex);
                    break;
                }
                case "BICYCLING": {
                    holder.post_ex_icon.setImageResource(R.drawable.bike_ex);
                    break;
                }
            }


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
        Long postId;
        String type;
        Long userId;
        Boolean isOwned;
        ImageView postImage;
        TextView title;
        TextView message;
        LottieAnimationView lottieHeart;
        ImageButton comment_button;
        boolean switchOn = false;
        ImageButton kebab_button;
        ImageView post_ex_icon;
        TextView post_score;
        TextView post_calo;

        int itemType;

        SharedPreferences prefs;
        String jwt;
        Map<String, String> headers;
        boolean firstLoad = false;

        private void initHeaderForRequest(View itemView) {
            prefs = itemView.getContext().getSharedPreferences("UserData", MODE_PRIVATE);
            jwt = prefs.getString("jwt", null);
            headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + jwt);
        }


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
                kebab_button = itemView.findViewById(R.id.kebab_button);
                post_ex_icon = itemView.findViewById(R.id.post_ex_icon);
                post_score = itemView.findViewById(R.id.post_score);
                post_calo = itemView.findViewById(R.id.post_calo);


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
                        intent.putExtra("postId", postId);
                        intent.putExtra("type", type);
                        view.getContext().startActivity(intent);
                    }
                });
                kebab_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOwned) {
                            initHeaderForRequest(itemView);
                            initDeletePost(headers, postId);
                            Api.deletePost.clone().enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (!response.isSuccessful()) {
                                        Log.d("Fail", "Error" + response);
                                        return;
                                    }
                                    int position = getArrPosition(postId);
                                    arrayList.remove(position);
                                    notifyDataSetChanged();
                                    openPopup("Successful", "Delete post successful", Styles.SUCCESS, v);
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.d("Fail", t.getMessage());
                                }
                            });
                        }
                    }
                });
            } else {
                itemType = VIEW_TYPE_LOADING;
            }
        }
    }

    public int getArrPosition(Long postId) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (Objects.equals(arrayList.get(i).getPostId(), postId)) {
                return i;
            }
        }
        return -1;
    }

    private void openPopup(String heading, String description, Styles styles, View v) {
        if (v.getContext() == null) {
            return;
        }
        PopupDialog.getInstance(v.getContext())
                .setStyle(styles)
                .setHeading(heading)
                .setDescription(description)
                .setCancelable(true)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
    }
}
