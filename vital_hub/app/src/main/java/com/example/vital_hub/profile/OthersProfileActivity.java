package com.example.vital_hub.profile;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CountResponse;
import com.example.vital_hub.client.spring.objects.ProfileDetailResponse;
import com.example.vital_hub.client.spring.objects.ProfileResponse;
import com.example.vital_hub.friend.RequestFriendListAdapter;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersProfileActivity extends AppCompatActivity {
    PopupWindow popupWindow;
    View dimOverlay;
    ImageView backButton;
    ImageView profileImage;
    TextView description;
//    BottomNavigationView bottomNavigationView;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    ProfileDetailResponse profileDetailResponse;
    ProfileResponse profileResponse;
    private UserDetail fetchedOthersProfileDetail;
    private UserInfo fetchedOthersProfile;
    private CountResponse countResponse;
    String status;
    TextView name;
    TextView totalFriend;
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
        fetchFriends(Long.parseLong(getIntent().getStringExtra("id")));
        fetchOthersProfile(Long.parseLong(getIntent().getStringExtra("id")));
        fetchOthersProfileDetail(Long.parseLong(getIntent().getStringExtra("id")));

//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.profile);

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
        totalFriend = findViewById(R.id.friend_counter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.profile) {
//            return true;
//        } else if (item.getItemId() == R.id.home) {
//            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
//            overridePendingTransition(0, 0);
//            return true;
//        } else if (item.getItemId() == R.id.exercise) {
//            startActivity(new Intent(getApplicationContext(), ExerciseGeneralActivity.class));
//            overridePendingTransition(0, 0);
//            return true;
//        } else if (item.getItemId() == R.id.competition) {
//            startActivity(new Intent(getApplicationContext(), CompetitionActivity.class));
//            overridePendingTransition(0, 0);
//            return true;
//        } else {
//            return false;
//        }
//    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void openPopup(String heading, String description, Styles styles) {
        PopupDialog.getInstance(this)
                .setStyle(styles)
                .setHeading(heading)
                .setDescription(description)
                .setCancelable(true)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
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
                    Glide.with(OthersProfileActivity.this).load(fetchedOthersProfileDetail.getAvatar()).into(profileImage);
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
                openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
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
                            functionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    confirmationPopUp(view);
                                }
                            });
                            break;
                        case "PENDING":
                            functionButton.setText("Sent");
                            break;
                        case "INCOMING":
                            functionButton.setText("Respond");
                            functionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PopupMenu popupMenu = new PopupMenu(OthersProfileActivity.this, view);
                                    popupMenu.getMenuInflater().inflate(R.menu.respond_menu, popupMenu.getMenu());
                                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem menuItem) {
                                            if (menuItem.getItemId() == R.id.accept) {
                                                Api.initAcceptRequest(headers, fetchedOthersProfile.getId());
                                                Api.acceptRequest.clone().enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                                        if(response.isSuccessful()) {
                                                            Toast.makeText(view.getContext(), "Accept friend request successfully", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(getIntent());
                                                            RequestFriendListAdapter.requestActionListener.onAction();
                                                        } else {
                                                            openPopup("Error", "Error code: " + response.code(), Styles.FAILED);
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                                        openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
                                                    }
                                                });
                                            } else if (menuItem.getItemId() == R.id.deny) {
                                                Api.initDenyRequest(headers, fetchedOthersProfile.getId());
                                                Api.denyRequest.clone().enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                                        if(response.isSuccessful()) {
                                                            Toast.makeText(view.getContext(), "Denied friend request", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(getIntent());
                                                        } else {
                                                            openPopup("Error", "Error code: " + response.code(), Styles.FAILED);
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                                        openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
                                                    }
                                                });
                                            }
                                            return true;
                                        }
                                    });
                                    popupMenu.show();
                                }
                            });
                            break;
                        default:
                            functionButton.setText("Add friend");
                            functionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Api.initAddFriend(headers, fetchedOthersProfile.getId());
                                    Api.addFriend.clone().enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                            if(response.isSuccessful()) {
                                                Toast.makeText(view.getContext(), "Send request", Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition(0, 0);
                                                startActivity(getIntent());
                                            } else {
                                                openPopup("Error", "Error code: " + response.code(), Styles.FAILED);
                                            }
                                        }
                                        @Override
                                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                            openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
                                        }
                                    });
                                }
                            });
                            break;
                    }
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
            }
        });
    }
    private void fetchFriends(long id) {
        Api.initGetOthersTotalFriend(headers, id);
        Api.getOthersTotalFriend.clone().enqueue(new Callback<CountResponse>() {
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
                openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
            }
        });
    }

    public void confirmationPopUp(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.unfriend_confirmation_popup, null);

        // Create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        this.popupWindow = new PopupWindow(popupView, width, height, focusable);


        // Initialize dim overlay
        dimOverlay = getLayoutInflater().inflate(R.layout.dim_overlay,null);

        // Add the overlay to the root layout of your activity
        ViewGroup rootView = findViewById(android.R.id.content);
        rootView.addView(dimOverlay);
        dimOverlay.setVisibility(View.GONE);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        dimOverlay.setVisibility(View.VISIBLE);

        // Dismiss the popup window when touched
        popupWindow.setOnDismissListener(() -> {
            dimOverlay.setVisibility(View.GONE);
        });

        Button cancel = popupView.findViewById(R.id.cancel_button);
        Button confirm = popupView.findViewById(R.id.confirm_button);
        TextView confirmText = popupView.findViewById(R.id.confirmation_text);

        confirmText.setText("Are you sure you want to remove \n" + fetchedOthersProfile.getName() + " as your friend?");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api.initDeleteFriend(headers, fetchedOthersProfile.getId());
                Api.deleteFriend.clone().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Removed friend", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                        else {
                            openPopup("Error", "Error code: " + response.code(), Styles.FAILED);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
                    }
                });
            }
        });
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
