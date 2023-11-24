package com.example.vital_hub.running;

import static com.example.vital_hub.client.spring.controller.Api.initRetrofitAndController;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.vital_hub.utils.ExerciseType;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CompetitionDurationResponse;
import com.example.vital_hub.client.spring.objects.CompetitionMinDetailResponse;
import com.example.vital_hub.client.spring.objects.SaveExerciseAndCompetitionDto;
import com.example.vital_hub.competition.data.CompetitionMinDetail;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    int stepCount = 0, compeStepCount = 0, stepCountInit = 0;
    int stepGoal = 1000;
    int progress = 0;
    int previousCount = 0;
    String duration;
    Long currentCompetitionId;
    private Sensor stepCounterSensor;
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
    NotificationManager notificationManager;
    RemoteViews competingNotificationLayout, runningNotificationLayout;
    NotificationCompat.Builder builder, competingBuilder, runningBuilder;
    SaveExerciseAndCompetitionDto saveExerciseDto, saveExerciseForCompetitionDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        // Check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            // Ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 19);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Ask for permission
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 19);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            // Ask for permission
            requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE}, 19);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            // Ask for permission
            requestPermissions(new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 19);
        }

        scheduleInit();
        scheduleSave();


        // Check if step goal is set
        SharedPreferences sharedPreferences = getSharedPreferences("stepGoal", MODE_PRIVATE);
        if (sharedPreferences.getInt("stepGoal", 0) == 0) {
            showSelectGoalPopup(10000);
        } else {
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
                editor.putInt("progress", 0);
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


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        startOrStopButton.setOnClickListener(v -> {
            if (isRunningCompetition) {
                isRunningCompetition = false;
                startOrStopButton.setBackground(getDrawable(R.drawable.start_round_button));
                competitionTimer.cancel();
                timerTextView.setText(duration);
                calTextView.setText("0");
                distanceTextView.setText("0,00");
                circularSeekBar.setProgress(0);
                stepCountTextView.setText("0");
                builder = competingBuilder;
                competingNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
                competingNotificationLayout.setTextViewText(R.id.time_left, timerTextView.getText().toString());

                // Delete notification stop button
                competingBuilder.mActions.clear();
                notificationManager.notify(666, builder.build());

                // Save exercise
                saveExerciseForCompetitionDto = new SaveExerciseAndCompetitionDto();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                saveExerciseForCompetitionDto.setStartedAt(LocalDateTime.now().format(formatter));
                saveExerciseForCompetitionDto.setType(ExerciseType.RUNNING);
                saveExerciseForCompetitionDto.setStep(10000);
                float cal = 10000 * 0.065f;
                saveExerciseForCompetitionDto.setCalo(cal);
                saveExerciseForCompetitionDto.setCompetitionId(currentCompetitionId);
                saveExerciseAndCompetition(saveExerciseForCompetitionDto);

            } else {
                isRunningCompetition = true;
                previousCount = stepCount;
                compeStepCount = 0;
                stepCountTextView.setText(String.valueOf(compeStepCount));
                updateCalAndDistance(compeStepCount);
                startOrStopButton.setBackground(getDrawable(R.drawable.stop_round_button));
                handleWhenCompeting();

                // Add stop button to notification
                Intent stopIntent = new Intent(this, RunningActivity.class);
                stopIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                stopIntent.putExtra("stop", true);
                PendingIntent stopPendingIntent = PendingIntent.getActivity(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                competingBuilder.addAction(R.drawable.stop_round_button, "Stop", stopPendingIntent);
                notificationManager.notify(666, builder.build());
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getBooleanExtra("stop", false)) {
            isRunningCompetition = false;
            startOrStopButton.setBackground(getDrawable(R.drawable.start_round_button));
            competitionTimer.cancel();
            timerTextView.setText(duration);
            calTextView.setText("0");
            distanceTextView.setText("0,00");
            circularSeekBar.setProgress(0);
            stepCountTextView.setText("0");
            builder = competingBuilder;
            competingNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
            competingNotificationLayout.setTextViewText(R.id.time_left, timerTextView.getText().toString());

            // Delete notification stop button
            competingBuilder.mActions.clear();
            notificationManager.notify(666, builder.build());

            // Save exercise
            saveExerciseForCompetitionDto = new SaveExerciseAndCompetitionDto();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            saveExerciseForCompetitionDto.setStartedAt(LocalDateTime.now().format(formatter));
            saveExerciseForCompetitionDto.setType(ExerciseType.RUNNING);
            saveExerciseForCompetitionDto.setStep(10000);
            float cal = 10000 * 0.065f;
            saveExerciseForCompetitionDto.setCalo(cal);
            saveExerciseForCompetitionDto.setCompetitionId(currentCompetitionId);
            saveExerciseAndCompetition(saveExerciseForCompetitionDto);
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == stepCounterSensor) {

            stepCount = (int) event.values[0] - stepCountInit;
            if (!isCompeting) {
                stepCountTextView.setText(String.valueOf(stepCount));
                runningNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
                notificationManager.notify(666, runningBuilder.build());
                updateCalAndDistance(stepCount);
            } else if (isRunningCompetition) {
                compeStepCount = stepCount - previousCount;
                stepCountTextView.setText(String.valueOf(compeStepCount));
                circularSeekBar.setProgress(progress);
                competingNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
                competingNotificationLayout.setTextViewText(R.id.time_left, timerTextView.getText().toString());
                notificationManager.notify(666, competingBuilder.build());
                updateCalAndDistance(compeStepCount);
            } else {
                competingNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
                competingNotificationLayout.setTextViewText(R.id.time_left, timerTextView.getText().toString());
                notificationManager.notify(666, competingBuilder.build());
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

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        SharedPreferences sharedPreferences = getSharedPreferences("stepCount" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK), MODE_PRIVATE);
//        stepCount = sharedPreferences.getInt("stepCount", 0);
//        stepCountTextView.setText(String.valueOf(stepCount));
//        progress = Math.min((int) ((stepCount * 100) / stepGoal), 100);
//        circularSeekBar.setProgress(progress);
//        runningNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
//        builder.setProgress(100, progress, false);
//        notificationManager.notify(666, builder.build());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRunningCompetition) {
            competitionTimer.cancel();
        }

        notificationManager.cancel(666);
    }

    private void initCircularSeekBar() {
        circularSeekBar.setProgress(progress);
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if (progress >= 100) {
                    openPopup("Congratulate", "You have reached your goal!", Styles.SUCCESS);
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
        stepCountInit = sharedPreferences.getInt("stepCountInit", 0);
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
        statistics_layout = findViewById(R.id.statistics_layout);
        statistics_layout.getChildAt(0).setVisibility(ConstraintLayout.GONE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        competingNotificationLayout = new RemoteViews(getPackageName(), R.layout.competing_notification);
        runningNotificationLayout = new RemoteViews(getPackageName(), R.layout.running_notification);
        runningNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
        runningNotification();
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
            Api.initGetJoinedCompetitionRunning(headers);
            Api.getJoinedCompetitionRunning.clone().enqueue(new Callback<CompetitionMinDetailResponse>() {
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
                        openPopup("Oh no", String.valueOf(response.code()), Styles.FAILED);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CompetitionMinDetailResponse> call, @NonNull Throwable t) {
                    openPopup("Oh no", t.getMessage(), Styles.FAILED);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            openPopup("Oh no", e.getMessage(), Styles.FAILED);
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
                isCompeting = true;
                isRunningCompetition = false;
                competitionTitle.clearFocus();
                competingNotification();
                handleIfCompetition();
            } else {
                isCompeting = false;
                isRunningCompetition = false;
                competitionTitle.clearFocus();
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
        progress = Math.min((int) ((stepCount * 100) / stepGoal), 100);
        circularSeekBar.setProgress(progress);
        builder = runningBuilder;
        notificationManager.notify(666, builder.build());
    }

    private void handleIfCompetition() {
        isCompeting = true;
        isRunningCompetition = false;
        currentCompetitionId = Long.parseLong(competitionTitle.getText().toString().substring(competitionTitle.getText().toString().indexOf("#") + 1));
        getCompetitionDuration(currentCompetitionId);
        startOrStopButton.setBackground(getDrawable(R.drawable.start_round_button));
        startOrStopButton.setVisibility(AppCompatButton.VISIBLE);
        statistics_layout.getChildAt(0).setVisibility(ConstraintLayout.VISIBLE);
        calTextView.setText("0");
        distanceTextView.setText("0,00");
        circularSeekBar.setProgress(0);
        stepCountTextView.setText("0");
        updateCalAndDistance(compeStepCount);
        builder = competingBuilder;
        competingNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
        competingNotificationLayout.setTextViewText(R.id.time_left, timerTextView.getText().toString());
        notificationManager.notify(666, builder.build());
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

                // Update progress
                progress = Math.min((int) ((stepCount * 100) / stepGoal), 100);
                circularSeekBar.setProgress(progress);
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
                        builder = competingBuilder;
                        competingNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
                        competingNotificationLayout.setTextViewText(R.id.time_left, timerTextView.getText().toString());
                        notificationManager.notify(666, builder.build());
                    }
                } else {
                    openPopup("Oh no", response.message(), Styles.FAILED);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompetitionDurationResponse> call, @NonNull Throwable t) {
                openPopup("Oh no", t.getMessage(), Styles.FAILED);
            }
        });
    }

    private void handleWhenCompeting() {
        Long millis = convertTimeStringToMillis(duration);
        competitionTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(convertMillisToTimeString(millisUntilFinished));
                builder = competingBuilder;
                competingNotificationLayout.setTextViewText(R.id.steps, stepCountTextView.getText().toString());
                competingNotificationLayout.setTextViewText(R.id.time_left, timerTextView.getText().toString());
                // Update progress
                progress = Math.min((int) ((stepCount * 100) / stepGoal), 100);
                circularSeekBar.setProgress(progress);
                notificationManager.notify(666, builder.build());
            }

            @Override
            public void onFinish() {
                timerTextView.setText("0");
                isRunningCompetition = false;
                startOrStopButton.setBackground(getDrawable(R.drawable.start_round_button));
                circularSeekBar.setProgress(progress);
                openPopup("Finish", "Time over!", Styles.SUCCESS);
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

    public void scheduleSave() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(this, SaveStepCountReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void scheduleInit() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }


        Intent intent = new Intent(this, InitStepCountReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void runningNotification() {
        Intent intent = new Intent(this, RunningActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);


        runningBuilder = new NotificationCompat.Builder(this, "running")
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.vital_hub_logo)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(runningNotificationLayout)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        String channelId = "running_channel";
        NotificationChannel channel = new NotificationChannel(channelId, "Running Channel", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        runningBuilder.setChannelId(channelId);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 19);
            return;
        }
        notificationManager.notify(666, runningBuilder.build());
    }

    public void competingNotification() {
        // Back to activity intent
        Intent intent = new Intent(this, RunningActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        competingBuilder = new NotificationCompat.Builder(this, "competing")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.vital_hub_logo)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(competingNotificationLayout)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        String channelId = "competing_channel";
        NotificationChannel channel = new NotificationChannel(channelId, "Competing Channel", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        competingBuilder.setChannelId(channelId);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 19);
            return;
        }
        notificationManager.notify(666, competingBuilder.build());
    }

    public void saveExerciseAndCompetition(SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto) {
        Api.saveResultForCompetition(headers, saveExerciseAndCompetitionDto);
        Api.savedCompetitionResult.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    openPopup("Oh no", response.message(), Styles.FAILED);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                openPopup("Oh no", t.getMessage(), Styles.FAILED);
            }
        });
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


}


