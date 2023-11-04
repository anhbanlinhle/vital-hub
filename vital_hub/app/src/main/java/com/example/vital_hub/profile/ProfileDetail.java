package com.example.vital_hub.profile;

import static com.example.vital_hub.client.spring.controller.Api.initPutUpdateProfileDetail;
import static com.example.vital_hub.client.spring.controller.Api.initRetrofitAndController;
import static com.example.vital_hub.client.spring.controller.Api.updateUserProfile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.ProfileDetailResponse;
import com.example.vital_hub.helper.KeyboardHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    PopupWindow popupWindow;
    View dimOverlay;
    ViewGroup toolBar;
    ImageView profileImage;
    Calendar calendar;
    TextView cancel;
    EditText userName;
    EditText birthDate;
    EditText gmail;
    EditText phoneNumber;
    EditText height;
    EditText weight;
    TextView enableEdit;
    TextView save;
    EditText description;
    String[] gender = {"Female", "Male"};
    Spinner chooseGender;
    ColorStateList colorStateList;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    ProfileDetailResponse profileDetailResponse;
    private UserDetail fetchedUserProfileDetail;
    private UserDetail newInfo;
    EditText exercisePerDay;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.profile_detail);
        KeyboardHelper.setupKeyboardHiding(this);
        initHeaderForRequest();
        fetchUserProfileDetail();
        initRetrofitAndController(prefs.getString("server", "10.0.2.2"));

        description = findViewById(R.id.edit_description);
        chooseGender = findViewById(R.id.choose_gender);
        userName = findViewById(R.id.edit_username);
        birthDate = findViewById(R.id.edit_birthdate);
        gmail = findViewById(R.id.edit_email);
        phoneNumber = findViewById(R.id.edit_phonenum);
        height = findViewById(R.id.edit_height);
        weight = findViewById(R.id.edit_weight);
        cancel = findViewById(R.id.cancel);
        calendar = Calendar.getInstance();
        profileImage = findViewById(R.id.profile_image);
        chooseGender.setOnItemSelectedListener(this);
        enableEdit = findViewById(R.id.edit);
        exercisePerDay = findViewById(R.id.edit_exercise_per_day);

        toolBar = (ViewGroup) enableEdit.getParent();

        ViewGroup.LayoutParams params = enableEdit.getLayoutParams();



        colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_black));

        profileImage.setEnabled(false);
        description.setEnabled(false);
        userName.setEnabled(false);
        birthDate.setEnabled(false);
        chooseGender.setEnabled(false);
        gmail.setEnabled(false);
        phoneNumber.setEnabled(false);
        height.setEnabled(false);
        weight.setEnabled(false);
        exercisePerDay.setEnabled(false);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);

        chooseGender.setAdapter(adapter);


        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });

        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });

        birthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });

        gmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });

        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });

        height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });

        weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });

        exercisePerDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });

        //Set up new save TextView
        save = new TextView(this);
        save.setId(R.id.save);
        save.setText("Save");
        save.setTextSize(16);
        save.setTextColor(ContextCompat.getColor(this, R.color.color_green));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userName.getText())) {
                    userName.setError("Username is required!");
                } else if (TextUtils.isEmpty(birthDate.getText())) {
                    birthDate.setError("Birthdate is required!");
                } else if (TextUtils.isEmpty(phoneNumber.getText())) {
                    phoneNumber.setError("Phone number is required!");
                } else if (TextUtils.isEmpty(exercisePerDay.getText())) {
                    exercisePerDay.setText("0");
                    fetchUpdateProfileDetail();
                } else if (TextUtils.isEmpty(height.getText()) && TextUtils.isEmpty(weight.getText()) && TextUtils.isEmpty(exercisePerDay.getText())) {
                    height.setText("0");
                    weight.setText("0");
                    exercisePerDay.setText("0");
                    fetchUpdateProfileDetail();
                } else if (TextUtils.isEmpty(height.getText()) && TextUtils.isEmpty(exercisePerDay.getText())) {
                    height.setText("0");
                    exercisePerDay.setText("0");
                    fetchUpdateProfileDetail();
                } else if (TextUtils.isEmpty(weight.getText()) && TextUtils.isEmpty(exercisePerDay.getText())) {
                    weight.setText("0");
                    exercisePerDay.setText("0");
                    fetchUpdateProfileDetail();
                } else if (TextUtils.isEmpty(height.getText()) && TextUtils.isEmpty(weight.getText())) {
                    height.setText("0");
                    weight.setText("0");
                    fetchUpdateProfileDetail();
                } else if (TextUtils.isEmpty(height.getText())) {
                    height.setText("0");
                    fetchUpdateProfileDetail();
                } else if (TextUtils.isEmpty(weight.getText())) {
                    weight.setText("0");
                    fetchUpdateProfileDetail();
                } else {
                    fetchUpdateProfileDetail();
                }

                toolBar.addView(enableEdit);

                toolBar.removeView(save);

                profileImage.setEnabled(false);
                description.setEnabled(false);
                userName.setEnabled(false);
                birthDate.setEnabled(false);
                chooseGender.setEnabled(false);
                gmail.setEnabled(false);
                phoneNumber.setEnabled(false);
                height.setEnabled(false);
                weight.setEnabled(false);
                exercisePerDay.setEnabled(false);

            }
        });


        enableEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolBar.removeView(enableEdit);
                toolBar.addView(save, params);
                save.setVisibility(View.VISIBLE);

                profileImage.setEnabled(true);
                profileImage.requestFocus();

                description.setEnabled(true);
                description.requestFocus();

                userName.setEnabled(true);
                userName.requestFocus();

                birthDate.setEnabled(true);
                birthDate.requestFocus();

                phoneNumber.setEnabled(true);
                phoneNumber.requestFocus();

                height.setEnabled(true);
                height.requestFocus();

                weight.setEnabled(true);
                weight.requestFocus();

                exercisePerDay.setEnabled(true);
                exercisePerDay.requestFocus();

            }
        });
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ProfileDetail.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ProfileDetail.this, profileImage);
                popupMenu.getMenuInflater().inflate(R.menu.profile_image_options, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void initBodyForRequest() {
        newInfo = new UserDetail(
                fetchedUserProfileDetail.getId(),
                userName.getText().toString(),
                gmail.getText().toString(),
                fetchedUserProfileDetail.getSex(),
                phoneNumber.getText().toString(),
                fetchedUserProfileDetail.getAvatar(),
                birthDate.getText().toString(),
                new DetailedProfile(
                        fetchedUserProfileDetail.getUserDetail().getId(),
                        fetchedUserProfileDetail.getUserDetail().getUserId(),
                        Double.valueOf(height.getText().toString()),
                        Double.valueOf(weight.getText().toString()),
                        Integer.valueOf(exercisePerDay.getText().toString()),
                        description.getText().toString()));
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        birthDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void fetchUpdateProfileDetail() {
        initBodyForRequest();
        initPutUpdateProfileDetail(headers, newInfo);
        updateUserProfile.clone().enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.isSuccessful()) {
                    savePopUp(ProfileDetail.this.getWindow().getDecorView().getRootView());
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
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
                    userName.setText(fetchedUserProfileDetail.getName());
                    birthDate.setText(fetchedUserProfileDetail.getDob());
                    gmail.setText(fetchedUserProfileDetail.getGmail());
                    phoneNumber.setText(fetchedUserProfileDetail.getPhoneNo());
                    if (fetchedUserProfileDetail.getUserDetail().getDescription() != null) {
                        description.setText(fetchedUserProfileDetail.getUserDetail().getDescription());
                    } else {
                        description.setText("");
                    }
                    if (fetchedUserProfileDetail.getUserDetail().getCurrentHeight() != null) {
                        height.setText(String.valueOf(fetchedUserProfileDetail.getUserDetail().getCurrentHeight()));
                    } else {
                        height.setText("");
                    }
                    if (fetchedUserProfileDetail.getUserDetail().getCurrentWeight() != null) {
                        weight.setText(String.valueOf(fetchedUserProfileDetail.getUserDetail().getCurrentWeight()));
                    } else {
                        weight.setText("");
                    }
                    chooseGender.setSelection(fetchedUserProfileDetail.getSex().getPosition());
                    if (fetchedUserProfileDetail.getUserDetail().getExerciseDaysPerWeek() != null) {
                        exercisePerDay.setText(String.valueOf(fetchedUserProfileDetail.getUserDetail().getExerciseDaysPerWeek()));
                    } else {
                        exercisePerDay.setText("");
                    }
                    Glide.with(ProfileDetail.this).load(fetchedUserProfileDetail.getAvatar()).into(profileImage);
                }
            }

            @Override
            public void onFailure(Call<ProfileDetailResponse> call, Throwable t) {
                Toast.makeText(ProfileDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    public void savePopUp(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.save_popup, null);

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
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        autoClosePopup(2000);
    }

    private void autoClosePopup(int millisecond) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, millisecond);
    }
}
