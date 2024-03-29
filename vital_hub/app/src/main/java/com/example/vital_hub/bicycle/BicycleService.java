package com.example.vital_hub.bicycle;

import static com.example.vital_hub.bicycle.BicycleTrackerActivity.drawRoute;
import static com.example.vital_hub.bicycle.BicycleTrackerActivity.getResultsAndDisplay;
import static com.example.vital_hub.bicycle.BicycleTrackerActivity.latitude;
import static com.example.vital_hub.bicycle.BicycleTrackerActivity.longitude;
import static com.example.vital_hub.bicycle.BicycleTrackerActivity.updateMapCamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.vital_hub.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class BicycleService extends Service {
    private ArrayList<LatLng> locationList = new ArrayList<>();

    private String duration;

    private CountDownTimer competitionTimer;
    private final IBinder mBinder = new MapBinder();
    private static final String CHANNEL_ID = "7979";
    RemoteViews customLayout;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            latitude = locationResult.getLastLocation().getLatitude();
            longitude = locationResult.getLastLocation().getLongitude();

            BicycleUtils.CyclingResults results = BicycleUtils.calculateRouteInfo(locationList);

            bindDataLayout(results, duration);

            LatLng latLng = new LatLng(latitude, longitude);
            locationList.add(latLng);

            drawRoute(locationList);
            getResultsAndDisplay(locationList);

            updateMapCamera();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        duration = intent.getStringExtra("duration");
        if (duration != null) {
            initCountTime();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocationUpdates();
    }

    private void buildNotification() {
        String stop = "stop";
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        customLayout = new RemoteViews(getPackageName(), R.layout.bicycle_notification_layout);

        @SuppressLint("LaunchActivityFromNotification")
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.vital_hub_logo)
                .setCustomContentView(customLayout)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH);

        channel.setShowBadge(false);
        channel.setDescription("Tracking location");
        channel.setSound(null, null);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Notification notification = builder.build();

        startForeground(1, notification);
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(500);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.requestLocationUpdates(request, locationCallback, null);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        stopForeground(true);
        stopSelf();
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.removeLocationUpdates(locationCallback);
    }

    private void initCountTime() {
        BicycleUtils.CyclingResults results = BicycleUtils.calculateRouteInfo(locationList);
        Long millis = convertTimeStringToMillis(duration);
        competitionTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                duration = convertMillisToTimeString(millisUntilFinished);
                bindDataLayout(results, duration);
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private long convertTimeStringToMillis(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        return ((hours * 60L + minutes) * 60 + seconds) * 1000L;
    }

    private String convertMillisToTimeString(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void bindDataLayout(BicycleUtils.CyclingResults results, String duration) {
        customLayout = new RemoteViews(getPackageName(), R.layout.bicycle_notification_layout);
        customLayout.setTextViewText(R.id.lat, String.valueOf(latitude));
        customLayout.setTextViewText(R.id.lng, String.valueOf(longitude));
        customLayout.setTextViewText(R.id.distance, String.format("%.2f", results.distances));
        customLayout.setTextViewText(R.id.calories, String.format("%.2f", results.calories));
        customLayout.setTextViewText(R.id.time_left, duration);

        startForeground(1, new NotificationCompat.Builder(BicycleService.this, CHANNEL_ID)
                .setContentTitle("Vital Hub")
                .setContentText("Tracking location")
                .setOngoing(true)
                .setSmallIcon(R.drawable.vital_hub_logo)
                .setCustomContentView(customLayout)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build());
    }

    public class MapBinder extends Binder {
        public BicycleService getService() {
            return BicycleService.this;
        }
    }
}
