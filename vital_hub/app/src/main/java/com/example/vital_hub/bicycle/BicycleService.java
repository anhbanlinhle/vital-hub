package com.example.vital_hub.bicycle;

import static com.example.vital_hub.bicycle.BicycleTracker.lat;
import static com.example.vital_hub.bicycle.BicycleTracker.lng;
import static com.example.vital_hub.bicycle.BicycleTracker.latitude;
import static com.example.vital_hub.bicycle.BicycleTracker.longitude;
import static com.example.vital_hub.bicycle.BicycleTracker.updateMapCamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.widget.Toast;

import com.example.vital_hub.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class BicycleService extends Service {
    private final IBinder mBinder = new MapBinder();
    private static final String CHANNEL_ID = "2";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

        @SuppressLint("LaunchActivityFromNotification") NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Tracking location")
                .setOngoing(true)
                .setContentIntent(broadcastIntent);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.setShowBadge(false);
        channel.setDescription("Tracking location");
        channel.setSound(null, null);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        startForeground(1, builder.build());
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(500);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                String location = "Latitude : " + locationResult.getLastLocation().getLatitude() +

                        "\nLongitude : " + locationResult.getLastLocation().getLongitude();
                latitude = locationResult.getLastLocation().getLatitude();
                longitude = locationResult.getLastLocation().getLongitude();
                lat.setText(String.valueOf(latitude));
                lng.setText(String.valueOf(longitude));
                updateMapCamera();

                Toast.makeText(BicycleService.this, location, Toast.LENGTH_SHORT).show();
            }
        }, null);

    }

    public class MapBinder extends Binder {
        public BicycleService getService() {
            return BicycleService.this;
        }
    }
}
