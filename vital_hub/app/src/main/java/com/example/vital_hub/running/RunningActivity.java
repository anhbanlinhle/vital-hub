package com.example.vital_hub.running;

import static com.example.vital_hub.client.spring.controller.Api.initRetrofitAndController;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CompetitionDurationResponse;
import com.example.vital_hub.client.spring.objects.CompetitionMinDetailResponse;
import com.example.vital_hub.competition.data.CompetitionMinDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.angrybyte.numberpicker.view.ActualNumberPicker;
import me.tankery.lib.circularseekbar.CircularSeekBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RunningActivity extends AppCompatActivity implements SensorEventListener {
    List<String> items = new ArrayList<>(Collections.singletonList("None"));
    private SensorManager sensorManager;
    private Boolean isCompeting = false, isRunningCompetition = false;
    private TextView stepCountTextView, timerTextView, distanceTextView, calTextView;
    int stepCount = 0, compeStepCount = 0;
    int stepGoal = 1000;
    int progress = 0;
    int previousCount = 0;
    String duration;
    Long currentCompetitionId;
    private Sensor stepCounterSensor, stepDetectorSensor;
    private AppCompatButton startOrStopButton, backButton;
    private CircularSeekBar circularSeekBar;
    private AutoCompleteTextView competitionTitle;
    private ArrayAdapter<String> adapter;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    List<CompetitionMinDetail> competitionMinDetails;
    LinearLayout statistics_layout;
    EditText goalTextView;
    private CountDownTimer competitionTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        // Check if step goal is set
        SharedPreferences sharedPreferences = getSharedPreferences("stepGoal", MODE_PRIVATE);
        if (sharedPreferences.getInt("stepGoal", 0) == 0) {
            showSelectGoalPopup(10000);
        }
        else {
            stepGoal = sharedPreferences.getInt("stepGoal", 1000);
        }

            // Test on remote device
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        initRetrofitAndController(prefs.getString("server", "10.0.2.2"));
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && getSharedPreferences("stepCount2", MODE_PRIVATE).getInt("stepCount", 0) != 0) {
            for (int i = 1; i <= 7; i++) {
                sharedPreferences = getSharedPreferences("stepCount" + i, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("stepCount", 0);
                editor.apply();
            }
        }

        // Init
        init7DaysCircularSeekBar();
        initActivity();
        initCircularSeekBar();
        initHeaderForRequest();


        getCompetitionTitleList();
        adapter = new ArrayAdapter<>(RunningActivity.this, android.R.layout.simple_list_item_1, items);
        competitionTitle.setAdapter(adapter);
        competitionTitle.setDropDownHeight(adapter.getCount() > 3 ? 450 : adapter.getCount() * 150);
        competitionTitle.setText(items.get(0), false);
        compeTitleOnClick();

        // Check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            // Ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 19);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        startOrStopButton.setOnClickListener(v -> {
            if (isRunningCompetition) {
                isRunningCompetition = false;
                startOrStopButton.setBackground(getDrawable(R.drawable.start_round_button));
                competitionTimer.cancel();
                timerTextView.setText(duration);
            } else {
                isRunningCompetition = true;
                previousCount = stepCount;
                startOrStopButton.setBackground(getDrawable(R.drawable.stop_round_button));
                handleWhenCompeting();
            }
        });

        backButton.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == null) {
            Toast.makeText(this, "No step counter sensor detected", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == stepCounterSensor) {
            stepCount = (int) event.values[0];
            if (!isCompeting) {
                stepCountTextView.setText(String.valueOf(stepCount));
                updateCalAndDistance(stepCount);
            }
            else if (isRunningCompetition){
                compeStepCount = stepCount - previousCount;
                stepCountTextView.setText(String.valueOf(compeStepCount));
                circularSeekBar.setProgress(progress);
                updateCalAndDistance(compeStepCount);
            } else {
                updateCalAndDistance(compeStepCount);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("stepCount" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("stepCount", stepCount);
        editor.putInt("progress", progress);
        editor.apply();
    }

    private void initCircularSeekBar() {
        circularSeekBar.setProgress(progress);
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if (progress >= 100) {
                    Toast.makeText(RunningActivity.this, "You have reached your goal!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }

    // Init activity
    private void initActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences("stepCount" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK), MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
        stepCountTextView = findViewById(R.id.steps_count);
        stepCountTextView.setText(String.valueOf(stepCount));
        backButton = findViewById(R.id.return_button);
        goalTextView = findViewById(R.id.goal);
        SharedPreferences sharedPreferences1 = getSharedPreferences("stepGoal", MODE_PRIVATE);
        stepGoal = sharedPreferences1.getInt("stepGoal", 1000);
        goalTextView.setText("/" + String.valueOf(stepGoal));
        goalTextView.setCursorVisible(false);
        goalTextView.setFocusable(false);
        goalTextView.setKeyListener(null);
        goalTextView.setLongClickable(true);
        goalTextView.setBackgroundResource(android.R.color.transparent);
        goalTextView.setOnClickListener(v -> showSelectGoalPopup(Integer.parseInt(goalTextView.getText().toString().substring(1))));
        startOrStopButton = findViewById(R.id.start_stop_button);
        startOrStopButton.setVisibility(AppCompatButton.INVISIBLE);
        circularSeekBar = findViewById(R.id.running_progress);
        timerTextView = findViewById(R.id.time);
        distanceTextView = findViewById(R.id.distance);
        calTextView = findViewById(R.id.calories);
        competitionTitle = findViewById(R.id.auto_complete_txt);
        statistics_layout = (LinearLayout) findViewById(R.id.statistics_layout);
        statistics_layout.getChildAt(0).setVisibility(ConstraintLayout.GONE);
        handleIfNone();


        if (stepCount > stepGoal) {
            progress = 100;
        } else {
            progress = (int) ((stepCount * 100) / stepGoal);
        }
        circularSeekBar.setProgress(progress);
    }

    //Init 7 days step count circular seek bar
    private void init7DaysCircularSeekBar() {
        CircularSeekBar[] circularSeekBars = new CircularSeekBar[7];
        circularSeekBars[0] = findViewById(R.id.sunday_progress);
        circularSeekBars[1] = findViewById(R.id.monday_progress);
        circularSeekBars[2] = findViewById(R.id.tuesday_progress);
        circularSeekBars[3] = findViewById(R.id.wednesday_progress);
        circularSeekBars[4] = findViewById(R.id.thursday_progress);
        circularSeekBars[5] = findViewById(R.id.friday_progress);
        circularSeekBars[6] = findViewById(R.id.saturday_progress);

        int[] progress = new int[7];
        for (int i = 1; i <= 7; i++) {
            SharedPreferences sharedPreferences = getSharedPreferences("stepCount" + i, MODE_PRIVATE);
            progress[i - 1] = (int) (sharedPreferences.getInt("progress", 0));
            circularSeekBars[i - 1].setProgress(progress[i - 1]);
        }

    }

    private void getCompetitionTitleList() {
        try {
            Api.initGetCompetitionTitleList(headers);
            Api.getCompetitionTitleList.clone().enqueue(new Callback<CompetitionMinDetailResponse>() {
                @Override
                public void onResponse(@NonNull Call<CompetitionMinDetailResponse> call, @NonNull Response<CompetitionMinDetailResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            competitionMinDetails = response.body().getData();
                            if (competitionMinDetails != null) {

                                for (int i = 0; i < competitionMinDetails.size(); i++) {
                                    items.add(competitionMinDetails.get(i).getTitle() + "  #" + competitionMinDetails.get(i).getId());
                                }
                                adapter.notifyDataSetChanged();
                                competitionTitle.setDropDownHeight(adapter.getCount() > 3 ? 450 : adapter.getCount() * 150);
                            }
                        }
                    } else {
                        Toast.makeText(RunningActivity.this, "Error" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CompetitionMinDetailResponse> call, @NonNull Throwable t) {
                    Toast.makeText(RunningActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void compeTitleOnClick() {
        competitionTitle.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            competitionTitle.setText(selected, false);
            if (position != 0) {
                handleIfCompetition();
            } else {
                handleIfNone();
            }
        });
    }

    private void handleIfNone() {
        isCompeting = false;
        isRunningCompetition = false;
        startOrStopButton.setVisibility(AppCompatButton.INVISIBLE);
        statistics_layout.getChildAt(0).setVisibility(ConstraintLayout.GONE);
        stepCountTextView.setText(String.valueOf(stepCount));
        progress = (int) ((stepCount * 100) / stepGoal);
        circularSeekBar.setProgress(progress);
    }

    private void handleIfCompetition() {
        isCompeting = true;
        isRunningCompetition = false;
        currentCompetitionId = Long.parseLong(competitionTitle.getText().toString().substring(competitionTitle.getText().toString().indexOf("#") + 1));
        getCompetitionDuration(currentCompetitionId);
        startOrStopButton.setBackground(getDrawable(R.drawable.start_round_button));
        startOrStopButton.setVisibility(AppCompatButton.VISIBLE);
        statistics_layout.getChildAt(0).setVisibility(ConstraintLayout.VISIBLE);
        stepCountTextView.setText(String.valueOf(compeStepCount));
        updateCalAndDistance(compeStepCount);
        circularSeekBar.setProgress(progress);
    }

    void showSelectGoalPopup(Integer goal) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_goal_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setCancelable(false);
        dialog.show();

        AppCompatButton confirmButton = dialog.findViewById(R.id.start_button);
        ActualNumberPicker numberPicker = dialog.findViewById(R.id.actual_picker);
        numberPicker.setValue(goal);

        confirmButton.setOnClickListener(v -> {
            if (numberPicker.getValue() > 1000) {
                stepGoal = numberPicker.getValue();
                goalTextView.setText("/" + String.valueOf(stepGoal));
                SharedPreferences sharedPreferences = getSharedPreferences("stepGoal", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("stepGoal", stepGoal);
                editor.apply();
                dialog.dismiss();
            } else {
                TextView what = dialog.findViewById(R.id.what);
                what.setVisibility(TextView.VISIBLE);
                // Set shake animation for popup
                final Animation animShake = AnimationUtils.loadAnimation(this, R.animator.shaking);
                what.startAnimation(animShake);
            }
        });
    }

    private void getCompetitionDuration(Long id) {

        Api.initGetCompetitionDuration(headers, id);
        Api.getCompetitionDuration.clone().enqueue(new Callback<CompetitionDurationResponse>() {
            @Override
            public void onResponse(@NonNull Call<CompetitionDurationResponse> call, @NonNull Response<CompetitionDurationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        duration = response.body().getDuration();
                        timerTextView.setText(duration);
                    }
                } else {
                    Toast.makeText(RunningActivity.this, "Error" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompetitionDurationResponse> call, @NonNull Throwable t) {
                Toast.makeText(RunningActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleWhenCompeting() {
        Long millis = convertTimeStringToMillis(duration);
        competitionTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(convertMillisToTimeString(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("0");
                isRunningCompetition = false;
                startOrStopButton.setBackground(getDrawable(R.drawable.start_round_button));
                circularSeekBar.setProgress(progress);
                Toast.makeText(RunningActivity.this, "Finish!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    public static long convertTimeStringToMillis(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        return ((hours * 60L + minutes) * 60 + seconds) * 1000L;
    }

    public static String convertMillisToTimeString(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void updateCalAndDistance(Integer stepCount) {
        // Running
        double distance = stepCount * 0.0008;
        double cal = stepCount * 0.065;
        distanceTextView.setText(String.format("%.2f", distance));
        calTextView.setText(String.format("%.0f", cal));
        progress = Math.min((int) ((stepCount * 100) / stepGoal), 100);
        circularSeekBar.setProgress(progress);
    }

}
