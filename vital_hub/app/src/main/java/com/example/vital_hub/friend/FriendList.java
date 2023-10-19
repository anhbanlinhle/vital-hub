package com.example.vital_hub.friend;


import com.example.vital_hub.model.Friend;
import com.example.vital_hub.client.controller.Api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vital_hub.client.objects.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.vital_hub.helper.*;


public class FriendList extends AppCompatActivity {

    private int limit = 10;
    private int offset = 0;
    private RecyclerView friendList;
    private Button returnButton, findButton;
    private ArrayList<Friend> fetchedFriendList;
    private FriendListAdapter friendListAdapter;
    private TextView totalFriend;
    private EditText searchFriend;
    private CountResponse countResponse;
    private FriendListResponse friendListResponse;
    private LinearLayoutManager layoutManager;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // Helper
        KeyboardHelper.setupKeyboardHiding(this);

        friendList = findViewById(R.id.friend_list);
        returnButton = findViewById(R.id.return_button);
        findButton = findViewById(R.id.find_friend);
        totalFriend = findViewById(R.id.total_friend);
        searchFriend = findViewById(R.id.search);
        layoutManager = new LinearLayoutManager(this);

        // TODO: fetch friend list from server
        fetchedFriendList = new ArrayList<>();
        initHeaderForRequest();

        // Set jwt for request
        Api.initJwt(headers);

        // Get total friend
        Api.initGetTotalFriend(headers);
        Api.getTotalFriend.clone().enqueue(new Callback<CountResponse>() {
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
        fetchFriendList(null, limit, offset);
        friendListAdapter = new FriendListAdapter(fetchedFriendList);

        friendList.setAdapter(friendListAdapter);
        friendList.setLayoutManager(layoutManager);
        // When user scroll to bottom of list, load more friend
        friendList.addOnScrollListener(
                new EndlessScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore() {
                        offset += limit;
                        fetchFriendList(null, limit, offset);
                    }
                }
        );


        // TODO: search friend
        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchedFriendList.clear();
                fetchFriendList(s.toString(), 10, 0);
                friendList.setAdapter(friendListAdapter);

                // When user scroll to bottom of list, load more friend
                friendList.addOnScrollListener(new EndlessScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore() {
                        offset += limit;
                        fetchFriendList(s.toString(), limit, offset);
                        friendListAdapter.notifyDataSetChanged();
                    }
                });


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    private void fetchFriendList(String name, Integer limit, Integer offset) {
        Api.initGetFriendList(headers, name, limit, offset);
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
    }
}
