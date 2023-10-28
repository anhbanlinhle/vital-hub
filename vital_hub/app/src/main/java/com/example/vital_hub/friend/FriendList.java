package com.example.vital_hub.friend;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.controller.Api;
import com.example.vital_hub.client.objects.CountResponse;
import com.example.vital_hub.client.objects.FriendListResponse;
import com.example.vital_hub.helper.EndlessScrollListener;
import com.example.vital_hub.helper.KeyboardHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FriendList extends AppCompatActivity implements FriendListAdapter.FriendActionListener {

    private final int limit = 10;
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
    static Map<String, String> headers;

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
        friendListAdapter.setFriendActionListener(this);

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
            Intent intent = new Intent(this, AddFriendActivity.class);
            intent.putExtra("friendList", fetchedFriendList);
            startActivity(intent);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchedFriendList.clear();
        fetchFriendList(null, limit, offset);
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchFriendList(String name, Integer limit, Integer offset) {
        fetchedFriendList.clear();
        if (name == null || name.isEmpty()) {

            Api.initGetFriendList(headers, name, limit, offset);

            Api.getFriendList.clone().enqueue(new Callback<FriendListResponse>() {
                                                  @Override
                                                  public void onResponse(@NonNull Call<FriendListResponse> call, @NonNull Response<FriendListResponse> response) {
                                                      if (response.isSuccessful()) {
                                                          friendListResponse = response.body();
                                                          assert friendListResponse != null;
                                                          fetchedFriendList.addAll(Arrays.asList(friendListResponse.getData()));
                                                          friendListAdapter.notifyDataSetChanged();
                                                      } else {
                                                          Toast.makeText(FriendList.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                                                      }
                                                  }

                                                  @Override
                                                  public void onFailure(@NonNull Call<FriendListResponse> call, @NonNull Throwable t) {
                                                      Toast.makeText(FriendList.this, "Failed to fetch friend list", Toast.LENGTH_SHORT).show();
                                                  }
                                              }
            );
        } else {
            Api.initGetSearchList(headers, name, limit, offset);
            Api.getSearchList.clone().enqueue(new Callback<FriendListResponse>() {
                                                  @Override
                                                  public void onResponse(@NonNull Call<FriendListResponse> call, @NonNull Response<FriendListResponse> response) {
                                                      if (response.isSuccessful()) {
                                                          friendListResponse = response.body();
                                                          assert friendListResponse != null;
                                                          fetchedFriendList.addAll(Arrays.asList(friendListResponse.getData()));
                                                          friendListAdapter.notifyDataSetChanged();
                                                      } else {
                                                          Toast.makeText(FriendList.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                                                      }
                                                  }

                                                  @Override
                                                  public void onFailure(@NonNull Call<FriendListResponse> call, @NonNull Throwable t) {
                                                      Toast.makeText(FriendList.this, "Failed to fetch friend list", Toast.LENGTH_SHORT).show();
                                                  }
                                              }
            );
        }


    }

    @Override
    public void onAction() {
        fetchedFriendList.clear();
        fetchFriendList(searchFriend.getText().toString(), 10, 0);
        friendList.setAdapter(friendListAdapter);
    }
}
