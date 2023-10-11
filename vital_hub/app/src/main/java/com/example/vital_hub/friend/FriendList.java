package com.example.vital_hub.friend;


import com.example.vital_hub.model.Friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.friend.*;
import java.util.ArrayList;
import java.util.List;


public class FriendList extends AppCompatActivity {

    private RecyclerView friendList;
    private Button returnButton, findButton;
    private ArrayList<Friend> fetchedFriendList;
    private FriendListAdapter friendListAdapter;
    private TextView totalFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        friendList = findViewById(R.id.friend_list);
        returnButton = findViewById(R.id.return_button);
        findButton = findViewById(R.id.find_friend);
        totalFriend = findViewById(R.id.total_friend);

        // TODO: fetch friend list from server
        fetchedFriendList = new ArrayList<>();
        fetchedFriendList.add(new Friend("Tuấn óc chó", "https://th.bing.com/th/id/OIP.Y9snZzdGtB8qkwaM0h6tSAHaEK?w=292&h=180&c=7&r=0&o=5&dpr=1.1&pid=1.7"));
        fetchedFriendList.add(new Friend("Tuấn óc cac", "https://th.bing.com/th/id/OIP.Y9snZzdGtB8qkwaM0h6tSAHaEK?w=292&h=180&c=7&r=0&o=5&dpr=1.1&pid=1.7"));
        fetchedFriendList.add(new Friend("Tuấn óc lon", "https://th.bing.com/th/id/OIP.Y9snZzdGtB8qkwaM0h6tSAHaEK?w=292&h=180&c=7&r=0&o=5&dpr=1.1&pid=1.7"));

        totalFriend.setText("Total friend: " + fetchedFriendList.size());
        friendListAdapter = new FriendListAdapter(fetchedFriendList);

        friendList.setAdapter(friendListAdapter);

        // TODO: return to previous activity
        returnButton.setOnClickListener(v -> {
            finish();
        });

        // TODO: find friend
        findButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindFriend.class);
            startActivity(intent);
        });
    }
}
