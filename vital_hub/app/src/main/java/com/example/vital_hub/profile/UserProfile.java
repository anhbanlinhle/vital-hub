package com.example.vital_hub.profile;

import static com.example.vital_hub.authentication.LoginScreen.oneTapClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.vital_hub.ExerciseActivity;
import com.example.vital_hub.R;
import com.example.vital_hub.TestPage;
import com.example.vital_hub.authentication.LoginScreen;
import com.example.vital_hub.client.controller.Api;
import com.example.vital_hub.client.objects.ProfileResponse;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.friend.FriendList;
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
    View activity;
    View statistic;
    View friend;
    Button editProfileBtn;
    BottomNavigationView bottomNavigationView;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;

    ProfileResponse profileResponse;

    Button shareButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        initHeaderForRequest();
        //NavBar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        editProfileBtn = findViewById(R.id.edit_profile);
        toolbar = findViewById(R.id.toolbar);
        setting = findViewById(R.id.setting);
        activity = findViewById(R.id.activity_view);
        statistic = findViewById(R.id.statistic_view);
        friend = findViewById(R.id.friend_view);
        shareButton = findViewById(R.id.share_button);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchUserProfile();
            }
        });
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, TestPage.class);
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
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, EditProfile.class);
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
            startActivity(new Intent(getApplicationContext(), ExerciseActivity.class));
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

    private void fetchUserProfile() {
        Api.initGetUserProfile(headers);
        Api.getUserProfile.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    profileResponse = response.body();
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }


}
