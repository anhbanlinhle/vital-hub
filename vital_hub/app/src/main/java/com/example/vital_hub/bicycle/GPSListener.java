package com.example.vital_hub.bicycle;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public interface GPSListener extends LocationListener, GpsStatus.Listener {
    void onLocationChanged(@NonNull Location location);

    void onProviderDisabled(@NonNull String provider);

    void onProviderEnabled(@NonNull String provider);

    void onStatusChanged(String provider, int status, Bundle extras);

    void onGpsStatusChanged(int event);
}
