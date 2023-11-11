package com.example.vital_hub.friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.FriendListResponse;
import com.example.vital_hub.helper.EndlessScrollListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends AppCompatActivity implements RequestFriendListAdapter.RequestActionListener{

    private final int limit = 10;
    private int offset = 0;
    private Button returnButton, findButton;
    private EditText friendName;
    private ArrayList<Friend> fetchedRequestList;
    private RecyclerView friendRequestRecycler;
    private FriendListResponse friendListResponse;
    private RequestFriendListAdapter requestListAdapter;
    private LinearLayoutManager layoutManager;
    SharedPreferences prefs;
    String jwt;
    static Map<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        // Return to previous activity
        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> {
            finish();
        });

//        // Find friend
//        findButton = findViewById(R.id.find_friend);
//        findButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, FindNewFriend.class);
//            startActivity(intent);
//        });

        // Friend request list
        friendRequestRecycler = findViewById(R.id.request_list);
        fetchedRequestList = new ArrayList<>();
        initHeaderForRequest();

        // Set up recycler view
        layoutManager = new LinearLayoutManager(this);
        friendRequestRecycler.setLayoutManager(layoutManager);
        requestListAdapter = new RequestFriendListAdapter(fetchedRequestList);
        requestListAdapter.setRequestActionListener(this);
        friendRequestRecycler.setAdapter(requestListAdapter);

        // Fetch friend request list
        fetchRequestList(limit, offset);

        // When scroll to bottom, fetch more friend request list
        friendRequestRecycler.addOnScrollListener(
                new EndlessScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore() {
                        offset += limit;
                        fetchRequestList(limit, offset);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchedRequestList.clear();
        fetchRequestList(limit, offset);
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchRequestList(Integer limit, Integer offset) {
        // Fetch friend request list
        Api.initGetFriendRequestList(headers, limit, offset);
        Api.getFriendRequestList.clone().enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(@NonNull Call<FriendListResponse> call,@NonNull Response<FriendListResponse> response) {
                if (response.isSuccessful()) {
                    friendListResponse = response.body();
                    assert friendListResponse != null;
                    fetchedRequestList.addAll(Arrays.asList(friendListResponse.getData()));
                    requestListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AddFriendActivity.this, "Failed to fetch friend request list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
                Toast.makeText(AddFriendActivity.this, "Failed to fetch friend request list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAction() {
        fetchedRequestList.clear();
        fetchRequestList(limit, offset);
    }
}
