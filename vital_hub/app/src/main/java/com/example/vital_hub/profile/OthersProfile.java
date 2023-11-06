package com.example.vital_hub.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.ProfileDetailResponse;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersProfile extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ImageView backButton;
    ImageView profileImage;

    TextView description;
    BottomNavigationView bottomNavigationView;

    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    ProfileDetailResponse profileDetailResponse;
    private UserDetail fetchedOthersProfileDetail;
    private UserDetail fetchedOthersProfileId;

    TextView name;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_profile);
        initHeaderForRequest();
        fetchOthersProfileDetail(fetchOthersProfileId());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        name = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        backButton = findViewById(R.id.back_button);
        description = findViewById(R.id.description);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void openOthersProfile() {
        startActivity(new Intent(this, OthersProfile.class));
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

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchOthersProfileDetail(long id) {
        Api.initGetOthersProfileDetail(headers, id);
        Api.getOthersProfileDetail.clone().enqueue(new Callback<ProfileDetailResponse>() {
            @Override
            public void onResponse(Call<ProfileDetailResponse> call, Response<ProfileDetailResponse> response) {
                if (response.isSuccessful()) {
                    profileDetailResponse = response.body();
                    assert profileDetailResponse != null;
                    fetchedOthersProfileDetail = profileDetailResponse.getData();
                    name.setText(fetchedOthersProfileDetail.getName());
                    description.setText(fetchedOthersProfileDetail.getUserDetail().getDescription());
                    Glide.with(OthersProfile.this).load(fetchedOthersProfileDetail.getAvatar()).into(profileImage);
                }
            }

            @Override
            public void onFailure(Call<ProfileDetailResponse> call, Throwable t) {
                Toast.makeText(OthersProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private long fetchOthersProfileId() {
        Api.initGetOthersProfileDetail(headers, id);
        Api.getOthersProfileDetail.clone().enqueue(new Callback<ProfileDetailResponse>() {
            @Override
            public void onResponse(Call<ProfileDetailResponse> call, Response<ProfileDetailResponse> response) {
                if (response.isSuccessful()) {
                    profileDetailResponse = response.body();
                    fetchedOthersProfileId = profileDetailResponse.getData();
                    fetchedOthersProfileId.getId();
                }
            }
            @Override
            public void onFailure(Call<ProfileDetailResponse> call, Throwable t) {
                Toast.makeText(OthersProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return 0;
    }
}
