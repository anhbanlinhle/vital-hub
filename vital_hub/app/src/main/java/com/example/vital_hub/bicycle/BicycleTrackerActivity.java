package com.example.vital_hub.bicycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bosphere.fadingedgelayout.FadingEdgeLayout;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CompetitionMinDetailResponse;
import com.example.vital_hub.client.spring.objects.SaveExerciseAndCompetitionDto;
import com.example.vital_hub.competition.data.CompetitionMinDetail;
import com.example.vital_hub.utils.ExerciseType;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BicycleTrackerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static GoogleMap mMap;
    FadingEdgeLayout mapContainer;
    FragmentContainerView map;
    int expectedMapHeight;
    FusedLocationProviderClient fusedLocationClient;
    static double latitude;
    static double longitude;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    // Competition selector display
    List<String> items = new ArrayList<>(Collections.singletonList("None"));
    private AutoCompleteTextView competitionTitle;
    private ArrayAdapter<String> compeAdapter;
    List<CompetitionMinDetail> competitionMinDetails;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    AppCompatButton back;
    FloatingActionButton record;
    static TextView distance;
    static TextView calories;
    BottomAppBar bottomBar;
    ConstraintLayout navStats;
    LinearLayout cardStats;
    static Marker currentLocationMarker;

    private SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto;

    private Long competitionId;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static double distanceValue;

    private static double caloValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_map);

        initHeaderForRequest();
        findViewComponents();
        bindViewComponents();
        checkLocationPermission();
        configCompetitionSelector();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        updateMapCamera();
        recordTrackingButton();
    }

    protected void findViewComponents() {
        mapContainer = findViewById(R.id.map_container);
        map = findViewById(R.id.map);
        back = findViewById(R.id.back);
        competitionTitle = findViewById(R.id.auto_complete_txt);
        record = findViewById(R.id.record);
        bottomBar = findViewById(R.id.bottom_bar);
        navStats = findViewById(R.id.nav_stats);
        distance = findViewById(R.id.distance);
        calories = findViewById(R.id.calories);
        cardStats = findViewById(R.id.card_stats);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.color_green));
    }

    protected void bindViewComponents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tracking;
                prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                tracking = prefs.getString("tracking", "stop");
                if (tracking.equals("stop")) {
                    PopupDialog.getInstance(BicycleTrackerActivity.this)
                            .setStyle(Styles.ALERT)
                            .setHeading("Cycle Safely")
                            .setDescription("Keep your phone away while cycling. Stay alert for a safer ride.")
                            .setDismissButtonText("Got it")
                            .setCancelable(true)
                            .setTimeout(2)
                            .showDialog(new OnDialogButtonClickListener() {
                                @Override
                                public void onDismissClicked(Dialog dialog) {
                                    super.onDismissClicked(dialog);
                                }
                            });
                    prefs.edit().putString("tracking", "start").apply();

                    initValueSaveDto();

                    startService(new Intent(BicycleTrackerActivity.this, BicycleService.class));
                } else {
                    saveExerciseAndCompetitionDto.setCalo((float) caloValue);
                    saveExerciseAndCompetitionDto.setDistance((float) distanceValue);

                    PopupDialog.getInstance(BicycleTrackerActivity.this)
                        .setStyle(Styles.IOS)
                        .setHeading("Stop Bicycling...?")
                        .setDescription("Are you sure you want to stop?"+
                                " This action cannot be undone")
                        .setCancelable(true)
                        .setPositiveButtonText("Confirm")
                        .setPositiveButtonTextColor(R.color.color_red)
                        .setNegativeButtonText("Cancel")
                        .setNegativeButtonTextColor(R.color.color_green)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveClicked(Dialog dialog) {
                                super.onPositiveClicked(dialog);
                                prefs.edit().putString("tracking", "stop").apply();
                                stopService(new Intent(BicycleTrackerActivity.this, BicycleService.class));
                                recordTrackingButton();
                                handleSubmitResult();
                            }

                            @Override
                            public void onNegativeClicked(Dialog dialog) {
                                super.onNegativeClicked(dialog);
                            }
                        });


                }
                recordTrackingButton();
            }
        });
    }

    void recordTrackingButton() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String tracking = prefs.getString("tracking", "stop");
        if (tracking.equals("stop")) {
            record.setImageResource(R.drawable.bicycle_stop);
            record.setColorFilter(Color.parseColor("#FFFFFF"));
            record.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1DB954")));
        }
        else {
            record.setImageResource(R.drawable.bicycle_start);
            record.setColorFilter(Color.parseColor("#1DB954"));
            record.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        }
        expandCollapseMap(tracking);
        navBarStats(tracking);
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
                updateMapCamera();
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    protected static void updateMapCamera() {
        if (mMap == null) {
            return;
        }
        LatLng home = new LatLng(latitude, longitude);
        mMap.setBuildingsEnabled(true);

        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
            currentLocationMarker = null;
            currentLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(home)
                    .title("Your location"));
        }
        else {
            currentLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(home)
                    .title("Your location"));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(home)
                .zoom(19)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected void expandCollapseMap(String tracking) {
        ViewGroup.LayoutParams layoutParams = mapContainer.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        if (tracking.equals("start")) {
            expectedMapHeight = height - 250;
        }
        else {
            expectedMapHeight = 600;
        }
        ValueAnimator anim = ValueAnimator.ofInt(mapContainer.getMeasuredHeight(), +expectedMapHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                layoutParams.height = val;
                mapContainer.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(1000);
        anim.start();
    }

    void navBarStats(String tracking) {
        ViewGroup.LayoutParams navStatsParams = navStats.getLayoutParams();
        ViewGroup.LayoutParams bottomBarParams = bottomBar.getLayoutParams();
        int bottomBarHeight;
        int navStatsHeight;
        if (tracking.equals("start")) {
            navStatsHeight = 100;
            bottomBarHeight = 200;
        }
        else {
            navStatsHeight = 0;
            bottomBarHeight = 125;
        }

        ValueAnimator anim4 = ValueAnimator.ofInt(navStats.getMeasuredHeight(), +navStatsHeight);
        anim4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val4 = (Integer) valueAnimator.getAnimatedValue();
                navStatsParams.height = val4;
                navStats.setLayoutParams(navStatsParams);
            }
        });
        anim4.setDuration(1000);
        anim4.start();

        ValueAnimator anim3 = ValueAnimator.ofInt(bottomBar.getMeasuredHeight(), +bottomBarHeight);
        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val3 = (Integer) valueAnimator.getAnimatedValue();
                bottomBarParams.height = val3;
                bottomBar.setLayoutParams(bottomBarParams);
            }
        });
        anim3.setDuration(1000);
        anim3.start();
    }

    void cardStatsShrink (String tracking) {
        ViewGroup.LayoutParams cardStatsParams = cardStats.getLayoutParams();
        int cardStatsHeight;

        if (tracking.equals("start")) {
            cardStatsHeight = 0;
        }
        else {
            cardStatsHeight = 500;
        }

        ValueAnimator anim5 = ValueAnimator.ofInt(cardStats.getMeasuredWidth(), +cardStatsHeight);
        anim5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val5 = (Integer) valueAnimator.getAnimatedValue();
                cardStatsParams.height = val5;
                cardStats.setLayoutParams(cardStatsParams);
            }
        });

        anim5.setDuration(500);
        anim5.start();

    }

    protected void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(BicycleTrackerActivity.this, permissions, 1);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map));
    }

    void configCompetitionSelector() {
        getCompetitionTitleList();
        compeAdapter = new ArrayAdapter<>(BicycleTrackerActivity.this, android.R.layout.simple_list_item_1, items);
        competitionTitle.setAdapter(compeAdapter);
        competitionTitle.setDropDownHeight(compeAdapter.getCount() > 3 ? 450 : compeAdapter.getCount() * 150);
        competitionTitle.setText(items.get(0), false);
        compeTitleOnClick();
    }
    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }
    private void getCompetitionTitleList() {
        try {
            Api.initGetJoinedCompetitionBicycling(headers);
            Api.getJoinedCompetitionBicycling.clone().enqueue(new Callback<CompetitionMinDetailResponse>() {
                @Override
                public void onResponse(@NonNull Call<CompetitionMinDetailResponse> call, @NonNull Response<CompetitionMinDetailResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            competitionMinDetails = response.body().getData();
                            if (competitionMinDetails != null) {

                                for (int i = 0; i < competitionMinDetails.size(); i++) {
                                    items.add(competitionMinDetails.get(i).getTitle() + "  #" + competitionMinDetails.get(i).getId());
                                }
                                compeAdapter.notifyDataSetChanged();
                                competitionTitle.setDropDownHeight(compeAdapter.getCount() > 3 ? 450 : compeAdapter.getCount() * 150);
                            }
                        }
                    } else {
                        Toast.makeText(BicycleTrackerActivity.this, "Error" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CompetitionMinDetailResponse> call, @NonNull Throwable t) {
                    Toast.makeText(BicycleTrackerActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void compeTitleOnClick() {
        competitionTitle.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            competitionTitle.setText(selected, false);

            if (position == 0) {
                return;
            }
            int splitPos = 0;
            for (int i = selected.length() - 1; i >= 0; i--) {
                if (selected.charAt(i) == '#') {
                    splitPos = i;
                    break;
                }
            }
            competitionId = Long.parseLong(selected.substring(splitPos + 1));
            if (saveExerciseAndCompetitionDto != null) {
                saveExerciseAndCompetitionDto.setCompetitionId(competitionId);
            }

            if (position != 0) {
//                isCompeting = true;
//                isRunningCompetition = false;
                competitionTitle.clearFocus();
//                competingNotification();
//                handleIfCompetition();
            } else {
//                handleIfNone();
            }
        });
    }
    public static void drawRoute(ArrayList<LatLng> locations) {
        if (locations.size() < 2) {
            // Not enough points to draw a route
            return;
        }

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);

        for (LatLng location : locations) {
            polylineOptions.add(location);
        }

        mMap.addPolyline(polylineOptions);
    }

    public static void getResultsAndDisplay(ArrayList<LatLng> locations) {
        BicycleUtils.CyclingResults results = BicycleUtils.calculateRouteInfo(locations);
        distanceValue = results.distances;
        caloValue = results.calories;
        distance.setText(String.format("%.2f", results.distances) + " km");
        calories.setText(String.format("%.2f", results.calories) + " kcal");
    }

    private void handleSubmitResult() {
        if (saveExerciseAndCompetitionDto.getCompetitionId() != null) {
            Api.saveResultForCompetition(headers, saveExerciseAndCompetitionDto);

            Api.savedCompetitionResult.clone().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        openPopup("Well done", "Save result for competition successful", Styles.SUCCESS);
                    } else {
                        openPopup("Oh no", "Fail to save result for competition", Styles.FAILED);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    openPopup("Oh no", "Fail to save result for competition", Styles.FAILED);
                }
            });
        } else {
            Api.saveExercise(headers, saveExerciseAndCompetitionDto);

            Api.savedExercise.clone().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        openPopup("Well done", "Save bicycling result successful", Styles.SUCCESS);
                    } else {
                        openPopup("Oh no", "Fail to save bicycling result", Styles.FAILED);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    openPopup("Oh no", "Fail to save bicycling result", Styles.FAILED);
                }
            });
        }
    }

    private void initValueSaveDto() {
        saveExerciseAndCompetitionDto = new SaveExerciseAndCompetitionDto();
        saveExerciseAndCompetitionDto.setType(ExerciseType.BICYCLING);
        saveExerciseAndCompetitionDto.setStartedAt(LocalDateTime.now().format(formatter));
        saveExerciseAndCompetitionDto.setCompetitionId(competitionId);
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