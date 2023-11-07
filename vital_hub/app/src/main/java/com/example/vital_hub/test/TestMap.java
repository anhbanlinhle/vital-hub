package com.example.vital_hub.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;

import android.Manifest;
import android.animation.ValueAnimator;
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
    private GoogleMap mMap;
    FadingEdgeLayout mapContainer;
    FragmentContainerView map;
    ViewGroup.LayoutParams mapLayoutParams;
    ConstraintLayout screen;
    FusedLocationProviderClient fusedLocationClient;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.exercise);

        toolbar = findViewById(R.id.toolbar_bicycle);

        back = findViewById(R.id.back_to_home_from_biycle);
        logo = findViewById(R.id.logo);
        mapContainer = findViewById(R.id.map_container);
        map = findViewById(R.id.map);
        screen = findViewById(R.id.screen);

        mapLayoutParams = mapContainer.getLayoutParams();
        mapLayoutParams.height = 600;
        mapContainer.setLayoutParams(mapLayoutParams);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(TestMap.this, permissions, 1);
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        updateLocation();
        expandCollapseMap();
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

    @Override
    protected void onResume() {
        super.onResume();
        updateLocation();
    }

    protected void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(TestMap.this, permissions, 1);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                        else {
                            latitude = 20.997578735609597;
                            longitude = 105.80936462688419;
                        }
                    }
                });

        updateMapCamera();
    }

    protected void updateMapCamera() {
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
        int expectedFullHeight = 0;
        if (mapContainer.getMeasuredHeight() == 600) {
            expectedFullHeight = screen.getMeasuredHeight() - toolbar.getMeasuredHeight() - bottomNavigationView.getMeasuredHeight() + 150;
        }
        else {
            expectedFullHeight = 600;
        }
        ValueAnimator anim = ValueAnimator.ofInt(mapContainer.getMeasuredHeight(), +expectedFullHeight);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map));

        updateLocation();
        updateMapCamera();
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