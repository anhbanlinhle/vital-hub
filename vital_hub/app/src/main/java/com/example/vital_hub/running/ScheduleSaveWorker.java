package com.example.vital_hub.running;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ScheduleSaveWorker extends Worker {
    public ScheduleSaveWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        RunningActivity activity = new RunningActivity();
        activity.saveStepCount();
        return Result.success();
    }
}
