package com.example.vital_hub.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.ProfileDetailResponse;
import com.example.vital_hub.client.spring.objects.ProfileResponse;
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
    ProfileResponse profileResponse;
    private UserDetail fetchedOthersProfileDetail;
    private UserInfo fetchedOthersProfile;
    String status;
    TextView name;
    Button functionButton;
    TextView dob;
    TextView email;
    TextView phoneNum;
    TextView othersGender;
    TextView height;
    TextView weight;
    TextView exercisesPerDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_profile);
        initHeaderForRequest();
        fetchOthersProfile(Long.parseLong(getIntent().getStringExtra("id")));
        fetchOthersProfileDetail(Long.parseLong(getIntent().getStringExtra("id")));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        name = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        backButton = findViewById(R.id.back_button);
        description = findViewById(R.id.description);
        functionButton = findViewById(R.id.function_button);
        dob = findViewById(R.id.others_dob);
        othersGender = findViewById(R.id.others_gender);
        email = findViewById(R.id.others_email);
        phoneNum = findViewById(R.id.others_phonenum);
        height = findViewById(R.id.others_height);
        weight = findViewById(R.id.others_weight);
        exercisesPerDay = findViewById(R.id.others_exercise_per_day);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
                    if (fetchedOthersProfileDetail.getUserDetail().getDescription() == null) {
                        description.setText("");
                    } else {
                        description.setText(fetchedOthersProfileDetail.getUserDetail().getDescription());
                    }
                    Glide.with(OthersProfile.this).load(fetchedOthersProfileDetail.getAvatar()).into(profileImage);
                    dob.setText(fetchedOthersProfileDetail.getDob());
                    if (fetchedOthersProfileDetail.getSex().getPosition() == 0) {
                        othersGender.setText("Female");
                    } else {
                        othersGender.setText("Male");
                    }
                    email.setText(fetchedOthersProfileDetail.getGmail());
                    phoneNum.setText(fetchedOthersProfileDetail.getPhoneNo());
                    if (fetchedOthersProfileDetail.getUserDetail().getCurrentHeight() == null) {
                        height.setText("");
                    } else {
                        height.setText(String.valueOf(fetchedOthersProfileDetail.getUserDetail().getCurrentHeight()));
                    }
                    if (fetchedOthersProfileDetail.getUserDetail().getCurrentWeight() == null) {
                        weight.setText("");
                    } else {
                        weight.setText(String.valueOf(fetchedOthersProfileDetail.getUserDetail().getCurrentWeight()));
                    }
                    if (fetchedOthersProfileDetail.getUserDetail().getExerciseDaysPerWeek() == null) {
                        exercisesPerDay.setText("");
                    } else {
                        exercisesPerDay.setText(String.valueOf(fetchedOthersProfileDetail.getUserDetail().getExerciseDaysPerWeek()));
                    }
                }
            }
            @Override
            public void onFailure(Call<ProfileDetailResponse> call, Throwable t) {
                Toast.makeText(OthersProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchOthersProfile(long id) {
        Api.initGetOthersProfile(headers, id);
        Api.getOthersProfile.clone().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    profileResponse = response.body();
                    assert profileResponse != null;
                    fetchedOthersProfile = profileResponse.getData();
                    switch (fetchedOthersProfile.getStatus()) {
                        case "FRIEND":
                            functionButton.setText("Unfriend");
                            break;
                        case "PENDING":
                            functionButton.setText("Sent");
                        case "INCOMING":
                            functionButton.setText("Respond");
                        default:
                            functionButton.setText("Add friend");
                            break;
                    }
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(OthersProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
