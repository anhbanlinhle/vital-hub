package com.example.vital_hub.profile;

import static com.example.vital_hub.authentication.LoginScreen.oneTapClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.TestPage;
import com.example.vital_hub.authentication.LoginScreen;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CountResponse;
import com.example.vital_hub.client.spring.objects.ProfileDetailResponse;
import com.example.vital_hub.client.spring.objects.ProfileResponse;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.example.vital_hub.friend.FriendList;
import com.example.vital_hub.history.HistoryActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.test.TestMain;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    Toolbar toolbar;
    ImageView setting;
    View history;
    View statistic;
    View friend;
    Button profileDetailButton;
    BottomNavigationView bottomNavigationView;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    ProfileResponse profileResponse;
    ProfileDetailResponse profileDetailResponse;
    TextView name;
    TextView description;
    TextView totalFriend;
    ImageView profileImage;
    private UserInfo fetchedUserProfile;
    private CountResponse countResponse;
    private UserDetail fetchedUserProfileDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        initHeaderForRequest();
        fetchUserProfileDetail();
        fetchFriends();


        //NavBar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        profileDetailButton = findViewById(R.id.profile_detail_button);
        toolbar = findViewById(R.id.toolbar);
        setting = findViewById(R.id.setting);
        history = findViewById(R.id.history_view);
        statistic = findViewById(R.id.statistic_view);
        friend = findViewById(R.id.friend_view);
        name = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        totalFriend = findViewById(R.id.friend_counter);
        description = findViewById(R.id.description);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, TestPage.class);
                startActivity(intent);
            }
        });

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, FriendList.class);
                startActivity(intent);
            }
        });
        profileDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, ProfileDetail.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(UserProfile.this, setting);
                popupMenu.getMenuInflater().inflate(R.menu.setting_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.test) {
                            Intent intent = new Intent(UserProfile.this, TestMain.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.logout) {
                            signOut();
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            return true;
        } else if (item.getItemId() == R.id.home) {
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.exercise) {
            startActivity(new Intent(getApplicationContext(), ExerciseGeneralActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.competition) {
            startActivity(new Intent(getApplicationContext(), CompetitionActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else {
            return false;
        }
    }

    private void signOut() {
        oneTapClient.signOut();
        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(UserProfile.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchUserProfileDetail() {
        Api.initGetUserProfileDetail(headers);
        Api.getUserProfileDetail.clone().enqueue(new Callback<ProfileDetailResponse>() {
            @Override
            public void onResponse(Call<ProfileDetailResponse> call, Response<ProfileDetailResponse> response) {
                if (response.isSuccessful()) {
                    profileDetailResponse = response.body();
                    assert profileDetailResponse != null;
                    fetchedUserProfileDetail = profileDetailResponse.getData();
                    name.setText(fetchedUserProfileDetail.getName());
                    if (fetchedUserProfileDetail.getUserDetail().getDescription() == null) {
                        description.setText("");
                    } else {
                        description.setText(fetchedUserProfileDetail.getUserDetail().getDescription());
                    }
                    Glide.with(UserProfile.this).load(fetchedUserProfileDetail.getAvatar()).into(profileImage);
                }
            }

            @Override
            public void onFailure(Call<ProfileDetailResponse> call, Throwable t) {
                Toast.makeText(UserProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFriends() {
        Api.initGetTotalFriend(headers);
        Api.getTotalFriend.clone().enqueue(new Callback<CountResponse>() {
            @Override
            public void onResponse(Call<CountResponse> call, Response<CountResponse> response) {
                if (response.isSuccessful()) {
                    countResponse = response.body();
                    assert countResponse != null;
                    totalFriend.setText(String.valueOf(countResponse.getData()));
                }
            }

            @Override
            public void onFailure(Call<CountResponse> call, Throwable t) {
                Toast.makeText(UserProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
