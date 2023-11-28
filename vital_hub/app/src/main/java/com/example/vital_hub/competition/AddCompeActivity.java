package com.example.vital_hub.competition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.competition.data.CompetitionAdd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.vital_hub.helper.*;
import com.example.vital_hub.helper.ImgToUrl.ImageUploadTask;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

public class AddCompeActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private ImageUploadTask imageUploadTask;
    private ImageButton btnRun;

    private ImageButton btnBicyle;
    private ImageButton btnPushUp;

    private TextView compeTypeView;
    private EditText title;
    private EditText startAt;

    private EditText endAt;
    private EditText duration;

    private String compeType;

    private Button addImgBtn;
    private AppCompatButton addButton, backButton;
    private ImageView imageView;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;

    private final int addImgCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compe);

        //Helper
        KeyboardHelper.setupKeyboardHiding(this);

        initDeclaration();
        initHeaderForRequest();


        addButton.setOnClickListener(v -> {

            if (validateInput()) {
                CompetitionAdd competitionAdd = new CompetitionAdd();
                competitionAdd.setTitle(title.getText().toString());
                competitionAdd.setStartedAt(startAt.getText().toString());
                competitionAdd.setEndedAt(endAt.getText().toString());
                competitionAdd.setDuration(duration.getText().toString());
                competitionAdd.setType(compeType);
                uploadImageAndSetUrl(competitionAdd);

            }
        });

        backButton.setOnClickListener(v -> {
            finish();
        });

    }

    private void initDeclaration() {
        scrollView = findViewById(R.id.scroll_view);
        compeType = "";
        compeTypeView = findViewById(R.id.compe_type);
        btnRun = findViewById(R.id.btn_run);
        btnBicyle = findViewById(R.id.btn_bicycle);
        btnPushUp = findViewById(R.id.btn_pushup);
        title = findViewById(R.id.compe_title);
        startAt = findViewById(R.id.compe_start);
        endAt = findViewById(R.id.compe_end);
        duration = findViewById(R.id.compe_edit_duration);
        addImgBtn = findViewById(R.id.add_compe_img_btn);
        imageView = findViewById(R.id.compe_img_holder);
        addButton = findViewById(R.id.add);
        backButton = findViewById(R.id.back);

        buttonBinding();
    }


    private void buttonBinding() {
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!compeType.equals(btnRun.getContentDescription().toString())) {
                    compeType = btnRun.getContentDescription().toString();
                    compeTypeView.setText(compeType);
                    btnRun.setImageResource(R.drawable.baseline_directions_run_24_white);
                    btnRun.setBackgroundResource(R.drawable.full_rounded_corner_green_bg);

                    btnBicyle.setBackgroundResource(R.drawable.full_rounded_corner_green_border);
                    btnBicyle.setImageResource(R.drawable.baseline_directions_bike_24_green);

                    btnPushUp.setBackgroundResource(R.drawable.full_rounded_corner_green_border);
                    btnPushUp.setImageResource(R.drawable.baseline_accessibility_new_24_type);
                }
            }
        });

        btnBicyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!compeType.equals(btnBicyle.getContentDescription().toString())) {
                    compeType = btnBicyle.getContentDescription().toString();
                    compeTypeView.setText(compeType);
                    btnBicyle.setImageResource(R.drawable.baseline_directions_bike_24_white);
                    btnBicyle.setBackgroundResource(R.drawable.full_rounded_corner_green_bg);

                    btnRun.setBackgroundResource(R.drawable.full_rounded_corner_green_border);
                    btnRun.setImageResource(R.drawable.baseline_directions_run_24);

                    btnPushUp.setBackgroundResource(R.drawable.full_rounded_corner_green_border);
                    btnPushUp.setImageResource(R.drawable.baseline_accessibility_new_24_type);
                }
            }
        });

        btnPushUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!compeType.equals(btnPushUp.getContentDescription().toString())) {
                    compeType = btnPushUp.getContentDescription().toString();
                    compeTypeView.setText(compeType);
                    btnPushUp.setImageResource(R.drawable.baseline_accessibility_new_24_type_white);
                    btnPushUp.setBackgroundResource(R.drawable.full_rounded_corner_green_bg);

                    btnRun.setBackgroundResource(R.drawable.full_rounded_corner_green_border);
                    btnRun.setImageResource(R.drawable.baseline_directions_run_24);

                    btnBicyle.setBackgroundResource(R.drawable.full_rounded_corner_green_border);
                    btnBicyle.setImageResource(R.drawable.baseline_directions_bike_24_green);
                }
            }
        });

        addImgBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), addImgCode);

        });

        startAt.setOnClickListener(v -> {
            showDateTimeDialog(startAt);
        });

        endAt.setOnClickListener(v -> {
            showDateTimeDialog(endAt);
        });

        duration.setOnClickListener(v -> {
            showTimeDialog(duration);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                imageView.setImageURI(uri);
            }
        }
    }

    private void showDateTimeDialog(EditText dateTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);

                        dateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(AddCompeActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        };
        new DatePickerDialog(AddCompeActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int hour = 1;
        int minute = 30;

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddCompeActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);

                        String selectedDuration = (hourOfDay < 10 ? ("0" + hourOfDay) : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute);
                        duration.setText(selectedDuration);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private Boolean validateInput() {
        if (compeType.equals("")) {
            compeTypeView.setError("Please select competition type");
            scrollView.smoothScrollTo(0, compeTypeView.getTop());
            return false;
        }
        if (title.getText().toString().equals("")) {
            title.setError("Please enter title");
            title.setFocusable(true);
            title.setFocusableInTouchMode(true);
            title.requestFocus();
            return false;
        }
        if (duration.getText().toString().equals("")) {
            duration.setError("Please select duration");
            scrollView.smoothScrollTo(0, duration.getTop());
            return false;
        }
        if (startAt.getText().toString().equals("")) {
            startAt.setError("Please select start time");
            scrollView.smoothScrollTo(0, startAt.getTop());
            return false;
        }
        if (endAt.getText().toString().equals("")) {
            endAt.setError("Please select end time");
            scrollView.smoothScrollTo(0, endAt.getTop());
            return false;
        }
        if (imageView.getDrawable() == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
            scrollView.smoothScrollTo(0, imageView.getTop());
            return false;
        }
        return true;
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void addCompetition(CompetitionAdd competitionAdd) {
        Api.initAddCompetition(headers, competitionAdd);
        Api.addCompetition.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddCompeActivity.this, "Add competition successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddCompeActivity.this, "Add competition failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(AddCompeActivity.this, "Add competition failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImageAndSetUrl(CompetitionAdd competitionAdd) {
        PopupDialog dialog = PopupDialog.getInstance(this);
        dialog.setStyle(Styles.PROGRESS)
                .setProgressDialogTint(R.color.color_green)
                .setCancelable(false)
                .showDialog();
        imageUploadTask = new ImageUploadTask(imageView, new ImageUploadTask.ImageUploadCallback() {
            @Override
            public void onImageUploaded(String imageUrl) {
                competitionAdd.setBackground(imageUrl);
                addCompetition(competitionAdd);
                dialog.setStyle(Styles.SUCCESS)
                        .setHeading("Success")
                        .setDescription("Add competition successfully")
                        .setCancelable(true)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                finish();
                            }
                        });
            }

            @Override
            public void onUploadFailed() {
                Toast.makeText(AddCompeActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
        imageUploadTask.execute();

    }
}