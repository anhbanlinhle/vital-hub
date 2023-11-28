package com.example.vital_hub.running;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Calendar;

public class InitStepCountReceiver extends BroadcastReceiver implements SensorEventListener
{
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
            int stepCountInit = (int) event.values[0];
            // Init step count to shared preferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("stepCount" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("stepCountInit", stepCountInit);
            editor.apply();

            // Unregister listener
            sensorManager.unregisterListener(this);

//            context.unregisterReceiver(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
