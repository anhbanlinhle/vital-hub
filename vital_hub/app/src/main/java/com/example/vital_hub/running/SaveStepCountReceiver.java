package com.example.vital_hub.running;

import static android.content.Context.MODE_PRIVATE;

import static com.example.vital_hub.client.spring.controller.Api.initRetrofitAndController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.SaveExerciseAndCompetitionDto;
import com.example.vital_hub.utils.ExerciseType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveStepCountReceiver extends BroadcastReceiver implements SensorEventListener {
    private Context context;
    SensorManager sensorManager;
    Sensor stepCounterSensor;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            // Get step goal from shared preferences
            SharedPreferences sharedPreferencesGoal = context.getSharedPreferences("stepGoal", MODE_PRIVATE);

            SharedPreferences sharedPreferences = context.getSharedPreferences("stepCount" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK), MODE_PRIVATE);
            int stepCount = (int) event.values[0] - sharedPreferences.getInt("stepCountInit", 0);
            // Init step count to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("stepCount", stepCount);
            editor.putInt("progress", (int) (stepCount * 100.0 / sharedPreferencesGoal.getInt("stepGoal", 10000)));
            editor.apply();

            SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto = new SaveExerciseAndCompetitionDto();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startAt = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            saveExerciseAndCompetitionDto.setStartedAt(startAt.format(formatter));
            saveExerciseAndCompetitionDto.setType(ExerciseType.RUNNING);
            saveExerciseAndCompetitionDto.setStep(stepCount);
            float cal = (float) (stepCount * 0.065);
            saveExerciseAndCompetitionDto.setCalo(cal);
            saveExerciseAndCompetitionDto.setStep(stepCount);

            // Init headers
            SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
            initRetrofitAndController(prefs.getString("server", "10.0.2.2"));
            String jwt = prefs.getString("jwt", null);
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + jwt);

            Api.saveResultForCompetition(headers, saveExerciseAndCompetitionDto);
            Api.savedCompetitionResult.clone().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Error" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Unregister listener
            sensorManager.unregisterListener(this);

//            context.unregisterReceiver(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
