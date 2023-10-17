package com.example.vital_hub.friend;


import com.example.vital_hub.model.Friend;
import com.example.vital_hub.client.controller.Api;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.vital_hub.client.objects.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FriendList extends AppCompatActivity {

    private RecyclerView friendList;
    private Button returnButton, findButton;
    private ArrayList<Friend> fetchedFriendList;
    private FriendListAdapter friendListAdapter;
    private TextView totalFriend;
    private EditText searchFriend;
    private CountResponse countResponse;
    private FriendListResponse friendListResponse;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;

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
        initHeaderForRequest();

        // Get total friend
        Api.initGetTotalFriend(headers, String.valueOf(1));
        Api.getTotalFriend.enqueue(new Callback<CountResponse>() {
                @Override
                public void onResponse(Call<CountResponse> call, Response<CountResponse> response) {
                    if (response.isSuccessful()) {
                        countResponse = response.body();
                        totalFriend.setText("Total friend: " + countResponse.getData());
                    }
                }

                @Override
                public void onFailure(Call<CountResponse> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            }
        );

        // Get list friend
        Api.initGetFriendList(headers, Long.valueOf(1), 10, 0);
        Api.getFriendList.enqueue(new Callback<FriendListResponse>() {
                @Override
                public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                    if (response.isSuccessful()) {
                        friendListResponse = response.body();
                        for (Friend friend : friendListResponse.getData()) {
                            fetchedFriendList.add(friend);
                        }
                        friendListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<FriendListResponse> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            }
        );
        friendListAdapter = new FriendListAdapter(fetchedFriendList);

        friendList.setAdapter(friendListAdapter);

        // TODO: search friend
        searchFriend = findViewById(R.id.search);


        // TODO: return to previous activity
        returnButton.setOnClickListener(v -> {
            finish();
        });

        // TODO: find friend
        findButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindUser.class);
            intent.putExtra("friendList", fetchedFriendList);
            startActivity(intent);
        });
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }
}
