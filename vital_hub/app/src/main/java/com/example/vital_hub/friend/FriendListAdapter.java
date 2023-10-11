package com.example.vital_hub.friend;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;
import com.example.vital_hub.model.Friend;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>{

    private ArrayList<Friend> friendList;

    public FriendListAdapter(ArrayList<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendListAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);

        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.FriendViewHolder holder, int position) {

        holder.friendName.setText(friendList.get(position).getName());
        Glide.with(holder.friendAvatar.getContext()).load(friendList.get(position).getAvatar()).into(holder.friendAvatar);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        private TextView friendName;
        private ImageView friendAvatar;
        public FriendViewHolder(@NonNull View view) {
            super(view);

            friendName = view.findViewById(R.id.friendName);
            friendAvatar = view.findViewById(R.id.avatar);
        }
    }
}
