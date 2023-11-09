package com.example.vital_hub.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bosphere.fadingedgelayout.FadingEdgeLayout;
import com.example.vital_hub.R;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.profile.UserProfile;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class TestMap extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, OnMapReadyCallback {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    TextView back, logo;
    static GoogleMap mMap;
    FadingEdgeLayout mapContainer;
    FragmentContainerView map;
    ConstraintLayout screen;
    int expectedMapHeight;
    FusedLocationProviderClient fusedLocationClient;
    static double latitude;
    static double longitude;
    static TextView lat;
    static TextView lng;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    static int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_map);
        count = 0;

        findViewComponents();
        bindViewComponents();
        checkLocationPermission();

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.exercise);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        updateLocationBackground();
        updateMapCamera();
        expandCollapseMap();
    }

    protected void findViewComponents() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar_bicycle);
        back = findViewById(R.id.back_to_home_from_biycle);
        logo = findViewById(R.id.logo);
        mapContainer = findViewById(R.id.map_container);
        map = findViewById(R.id.map);
        screen = findViewById(R.id.screen);
        lat = findViewById(R.id.lat);
        lng = findViewById(R.id.lng);
    }

    protected void bindViewComponents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandCollapseMap();
            }
        });
    }

    protected void updateLocation() {
        checkLocationPermission();
        locationRequest = new LocationRequest()
                .setInterval(1000)
                .setFastestInterval(500)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location result = locationResult.getLastLocation();
                assert result != null;
                latitude = result.getLatitude();
                longitude = result.getLongitude();
                lat.setText(String.valueOf(latitude));
                lng.setText(String.valueOf(longitude));
                updateMapCamera();
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public static class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocationResult.hasResult(intent)) {
                LocationResult locationResult = LocationResult.extractResult(intent);
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        lat.setText(String.valueOf(count));
                        lng.setText(String.valueOf(count));
                        count++;
                        updateMapCamera();
                    }
                }
            }
        }
    }

    protected void updateLocationBackground() {
        checkLocationPermission();
        locationRequest = new LocationRequest()
                .setInterval(1000)
                .setFastestInterval(500)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Intent intent = new Intent(this, LocationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest, pendingIntent);
    }

    protected static void updateMapCamera() {
        if (mMap == null) {
            return;
        }
        LatLng home = new LatLng(latitude, longitude);
        mMap.setBuildingsEnabled(true);
        mMap.addMarker(new MarkerOptions()
                .position(home)
                .title("Your location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(home)
                .zoom(20)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected void expandCollapseMap() {
        if (mapContainer.getMeasuredHeight() == 600) {
            expectedMapHeight = screen.getMeasuredHeight() - toolbar.getMeasuredHeight() - bottomNavigationView.getMeasuredHeight() + 150;
        }
        else {
            expectedMapHeight = 600;
        }
        ValueAnimator anim = ValueAnimator.ofInt(mapContainer.getMeasuredHeight(), +expectedMapHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mapContainer.getLayoutParams();
                layoutParams.height = val;
                mapContainer.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(1000);
        anim.start();
    }

    protected void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(TestMap.this, permissions, 1);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), UserProfile.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.home) {
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.exercise) {
            return true;
        } else if (item.getItemId() == R.id.competition) {
            startActivity(new Intent(getApplicationContext(), CompetitionActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else {
            return false;
        }
    }
}