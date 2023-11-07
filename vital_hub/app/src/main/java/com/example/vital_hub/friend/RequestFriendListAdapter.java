package com.example.vital_hub.friend;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.profile.OthersProfile;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFriendListAdapter extends RecyclerView.Adapter<RequestFriendListAdapter.RequestFriendViewHolder>{
    private RequestActionListener requestActionListener;
    private final ArrayList<Friend> requestList;
    Map<String, String> headers = AddFriendActivity.headers;

    public RequestFriendListAdapter(ArrayList<Friend> requestList) {
        this.requestList = requestList;
    }

    public void setRequestActionListener(RequestActionListener requestActionListener) {
        this.requestActionListener = requestActionListener;
    }

    @NonNull
    @Override
    public RequestFriendListAdapter.RequestFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_friend_item, parent, false);
        return new RequestFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestFriendListAdapter.RequestFriendViewHolder holder, int position) {
        holder.requestName.setText(requestList.get(position).getName());
        Glide.with(holder.requestAvatar.getContext()).load(requestList.get(position).getAvatar()).into(holder.requestAvatar);

        holder.setItemClickListener((view, position1, isLongClick) -> {
            Intent intent = new Intent(view.getContext(), OthersProfile.class);
            intent.putExtra("id", requestList.get(position).getId()+"");
            view.getContext().startActivity(intent);
        });

        holder.acceptButton.setOnClickListener(v -> {
            Api.initAcceptRequest(headers, requestList.get(position).getId());
            Api.acceptRequest.clone().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()) {
                        requestActionListener.onAction();
                        Toast.makeText(v.getContext(), "Accept friend request successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(v.getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(v.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.denyButton.setOnClickListener(v -> {
            Api.initDenyRequest(headers, requestList.get(position).getId());
            Api.denyRequest.clone().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()) {
                        requestActionListener.onAction();
                        Toast.makeText(v.getContext(), "Deny friend request successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(v.getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(v.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView requestName;
        private final ImageView requestAvatar;
        private final Button acceptButton, denyButton;
        private ItemClickListener itemClickListener;

        public RequestFriendViewHolder(@NonNull View view) {
            super(view);

            requestName = view.findViewById(R.id.request_name);
            requestAvatar = view.findViewById(R.id.avatar);
            acceptButton = view.findViewById(R.id.accept);
            denyButton = view.findViewById(R.id.deny);

            view.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

    }

    public interface RequestActionListener {
        void onAction();
    }
}
