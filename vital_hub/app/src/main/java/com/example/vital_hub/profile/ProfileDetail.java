package com.example.vital_hub.profile;

import static com.example.vital_hub.client.spring.controller.Api.initPutUpdateProfileDetail;
import static com.example.vital_hub.client.spring.controller.Api.initRetrofitAndController;
import static com.example.vital_hub.client.spring.controller.Api.updateUserProfile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
        save = findViewById(R.id.save);
        exercisePerDay = findViewById(R.id.edit_exercise_per_day);

        colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_black));

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchUpdateProfileDetail();
            }
        });

        enableEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEdit.setClickable(false);
                enableEdit.setFocusable(false);
                enableEdit.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);

                description.setEnabled(true);
                description.requestFocus();

                userName.setEnabled(true);
                userName.requestFocus();

                birthDate.setEnabled(true);
                birthDate.requestFocus();

                gmail.setEnabled(true);
                gmail.requestFocus();

                phoneNumber.setEnabled(true);
                phoneNumber.requestFocus();

                height.setEnabled(true);
                height.requestFocus();

                weight.setEnabled(true);
                weight.requestFocus();

                exercisePerDay.setEnabled(true);
                exercisePerDay.requestFocus();


//                if (TextUtils.isEmpty(userName.getText())) {
//                    userName.setError("Username is required!");
//                } else {
//                    fetchUpdateProfileDetail();
//                }


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

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
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
                if (!response.isSuccessful()) {
                    Log.d("Fail", "Error" + response);
                    return;
                }
                Log.d("Fail", "Error" + response);
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
                    description.setText(fetchedUserProfileDetail.getUserDetail().getDescription());
                    height.setText(String.valueOf(fetchedUserProfileDetail.getUserDetail().getCurrentHeight()));
                    weight.setText(String.valueOf(fetchedUserProfileDetail.getUserDetail().getCurrentWeight()));
                    chooseGender.setSelection(fetchedUserProfileDetail.getSex().getPosition());
                    exercisePerDay.setText(String.valueOf(fetchedUserProfileDetail.getUserDetail().getExerciseDaysPerWeek()));
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
}
