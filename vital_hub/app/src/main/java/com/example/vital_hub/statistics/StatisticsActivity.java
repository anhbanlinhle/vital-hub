package com.example.vital_hub.statistics;

import static com.example.vital_hub.client.spring.controller.Api.initGetWeeklyStat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.WeeklyExercise;
import com.example.vital_hub.home_page.AddPostActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.home_page.HpRecyclerAdapter;
import com.example.vital_hub.utils.ExerciseType;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsActivity extends AppCompatActivity {
    static BarChart runningBarChart;
    static BarChart bikingBarChart;
    static BarChart gymBarChart;
    static BarChart pushupBarChart;
    static BarDataSet runningDataSet;
    static BarDataSet bikingDataSet;
    static BarDataSet gymDataSet;
    static BarDataSet pushupDataSet;

    ImageView runningIcon;
    ImageView bikeIcon;
    ImageView gymIcon;
    ImageView pushupIcon;
    static List<String> xValues = Arrays.asList("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    ImageButton back_button;
    TabLayout stat_tab_layout;
    static Map<ExerciseType, List<WeeklyExercise>> weeklyResult;
    static ArrayList<BarEntry> runningEntries = new ArrayList<>();
    static ArrayList<BarEntry> bikingEntries = new ArrayList<>();
    static ArrayList<BarEntry> gymEntries = new ArrayList<>();
    static ArrayList<BarEntry> pushupEntries = new ArrayList<>();
    static BarData runningBarData;
    static BarData bikingBarData;
    static BarData gymBarData;
    static BarData pushupBarData;
    static TextView runScoreText;
    static TextView bikeScoreText;
    static TextView gymScoreText;
    static TextView pushupScoreText;
    static TextView runScore;
    static TextView bikeScore;
    static TextView gymScore;
    static TextView pushupScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_layout);

        runningBarChart = findViewById(R.id.running_bar_chart);
        bikingBarChart = findViewById(R.id.bike_bar_chart);
        gymBarChart = findViewById(R.id.gym_bar_chart);
        pushupBarChart = findViewById(R.id.pushup_bar_chart);
        runningIcon = findViewById(R.id.stat_running_icon);
        bikeIcon = findViewById(R.id.stat_bike_icon);
        gymIcon = findViewById(R.id.stat_gym_icon);
        pushupIcon = findViewById(R.id.stat_pushup_icon);
        back_button = findViewById(R.id.stat_back_button);
        stat_tab_layout = findViewById(R.id.stat_tab);
        runScoreText = findViewById(R.id.stat_run_score_text);
        bikeScoreText = findViewById(R.id.stat_bike_score_text);
        gymScoreText = findViewById(R.id.stat_gym_score_text);
        pushupScoreText = findViewById(R.id.stat_pushup_score_text);
        runScore = findViewById(R.id.stat_run_score);
        bikeScore = findViewById(R.id.stat_bike_score);
        gymScore = findViewById(R.id.stat_gym_score);
        pushupScore = findViewById(R.id.stat_pushup_score);


        initHeaderForRequest();
        fetchStat();

        int bikeColor = Color.parseColor("#00c9bd");
        int gymColor = Color.parseColor("#ec9a3a");
        int pushupColor = Color.parseColor("#e05252");

        bikeIcon.setColorFilter(bikeColor);
        gymIcon.setColorFilter(gymColor);
        pushupIcon.setColorFilter(pushupColor);

        runningDataSet = new BarDataSet(runningEntries, "RUNNING");
        runningDataSet.setColors(Color.parseColor("#1DB954"));

        bikingDataSet = new BarDataSet(bikingEntries, "BIKE");
        bikingDataSet.setColors(Color.parseColor("#00c9bd"));

        gymDataSet = new BarDataSet(gymEntries, "GYM");
        gymDataSet.setColors(Color.parseColor("#ec9a3a"));

        pushupDataSet = new BarDataSet(pushupEntries, "PUSHUP");
        pushupDataSet.setColors(Color.parseColor("#e05252"));

        runningBarData = new BarData(runningDataSet);
        runningBarData.setBarWidth(0.25f);
        runningBarChart.setData(runningBarData);
        runningBarChart.getDescription().setEnabled(false);
        runningBarChart.getXAxis().setEnabled(true);
        runningBarChart.getAxisLeft().setDrawAxisLine(false);
        runningBarChart.getAxisRight().setDrawAxisLine(false);
        runningBarChart.getXAxis().setDrawAxisLine(false);
        runningBarChart.getAxisLeft().setDrawGridLines(false);
        runningBarChart.getXAxis().setDrawGridLines(false);
        runningBarChart.getAxisRight().setDrawGridLines(false);
        runningBarChart.getAxisLeft().setDrawLabels(false);
        runningBarChart.getAxisRight().setDrawLabels(false);
        runningBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        runningBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        runningBarChart.getXAxis().setGranularity(1);
        runningBarChart.getXAxis().setGranularityEnabled(true);
        runningBarChart.getLegend().setEnabled(false);

        gymBarData = new BarData(gymDataSet);
        gymBarData.setBarWidth(0.25f);
        gymBarChart.setData(gymBarData);
        gymBarChart.getDescription().setEnabled(false);
        gymBarChart.getXAxis().setEnabled(true);
        gymBarChart.getAxisLeft().setDrawAxisLine(false);
        gymBarChart.getAxisRight().setDrawAxisLine(false);
        gymBarChart.getXAxis().setDrawAxisLine(false);
        gymBarChart.getAxisLeft().setDrawGridLines(false);
        gymBarChart.getXAxis().setDrawGridLines(false);
        gymBarChart.getAxisRight().setDrawGridLines(false);
        gymBarChart.getAxisLeft().setDrawLabels(false);
        gymBarChart.getAxisRight().setDrawLabels(false);
        gymBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        gymBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        gymBarChart.getXAxis().setGranularity(1);
        gymBarChart.getXAxis().setGranularityEnabled(true);
        gymBarChart.getLegend().setEnabled(false);

        bikingBarData = new BarData(bikingDataSet);
        bikingBarData.setBarWidth(0.25f);
        bikingBarChart.setData(bikingBarData);
        bikingBarChart.getDescription().setEnabled(false);
        bikingBarChart.getXAxis().setEnabled(true);
        bikingBarChart.getAxisLeft().setDrawAxisLine(false);
        bikingBarChart.getAxisRight().setDrawAxisLine(false);
        bikingBarChart.getXAxis().setDrawAxisLine(false);
        bikingBarChart.getAxisLeft().setDrawGridLines(false);
        bikingBarChart.getXAxis().setDrawGridLines(false);
        bikingBarChart.getAxisRight().setDrawGridLines(false);
        bikingBarChart.getAxisLeft().setDrawLabels(false);
        bikingBarChart.getAxisRight().setDrawLabels(false);
        bikingBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        bikingBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bikingBarChart.getXAxis().setGranularity(1);
        bikingBarChart.getXAxis().setGranularityEnabled(true);
        bikingBarChart.getLegend().setEnabled(false);

        pushupBarData = new BarData(pushupDataSet);
        pushupBarData.setBarWidth(0.25f);
        pushupBarChart.setData(pushupBarData);
        pushupBarChart.getDescription().setEnabled(false);
        pushupBarChart.getXAxis().setEnabled(true);
        pushupBarChart.getAxisLeft().setDrawAxisLine(false);
        pushupBarChart.getAxisRight().setDrawAxisLine(false);
        pushupBarChart.getXAxis().setDrawAxisLine(false);
        pushupBarChart.getAxisLeft().setDrawGridLines(false);
        pushupBarChart.getXAxis().setDrawGridLines(false);
        pushupBarChart.getAxisRight().setDrawGridLines(false);
        pushupBarChart.getAxisLeft().setDrawLabels(false);
        pushupBarChart.getAxisRight().setDrawLabels(false);
        pushupBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        pushupBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        pushupBarChart.getXAxis().setGranularity(1);
        pushupBarChart.getXAxis().setGranularityEnabled(true);
        pushupBarChart.getLegend().setEnabled(false);

        stat_tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1) {
                    populatedData(1);
                } else {
                    populatedData(0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchStat() {
        initGetWeeklyStat(headers);
        Api.getWeeklyStat.clone().enqueue(new Callback<Map<ExerciseType, List<WeeklyExercise>>>() {
            @Override
            public void onResponse(Call<Map<ExerciseType, List<WeeklyExercise>>> call, Response<Map<ExerciseType, List<WeeklyExercise>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    weeklyResult = response.body();
                    populatedData(0);
                } else {
                    openPopup("Error", "Error code: " + response.code(), Styles.FAILED);
                }
            }

            @Override
            public void onFailure(Call<Map<ExerciseType, List<WeeklyExercise>>> call, Throwable t) {
                openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
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


    private static void populatedData(int tab) {
        if(tab == 0) {
            Integer totalStep = 0;
            Float totalDistance = 0F;
            Integer totalAttempts = 0;
            Integer totalReps = 0;

            runningEntries.clear();
            bikingEntries.clear();
            gymEntries.clear();
            pushupEntries.clear();

            runScoreText.setText("STEPS");
            bikeScoreText.setText("DISTANCE");
            gymScoreText.setText("ATTEMPTS");
            pushupScoreText.setText("REPS");


            for(int i=0; i<7; i++) {
                totalStep += Objects.requireNonNull(weeklyResult.get(ExerciseType.RUNNING)).get(i).getStep();
                totalDistance += Objects.requireNonNull(weeklyResult.get(ExerciseType.BICYCLING)).get(i).getDistance();
                totalAttempts += Objects.requireNonNull(weeklyResult.get(ExerciseType.GYM)).get(i).getGymGroup();
                totalReps += Objects.requireNonNull(weeklyResult.get(ExerciseType.PUSHUP)).get(i).getRep();

                runningEntries.add(new BarEntry(i, Objects.requireNonNull(weeklyResult.get(ExerciseType.RUNNING)).get(i).getStep()));
                bikingEntries.add(new BarEntry(i, Objects.requireNonNull(weeklyResult.get(ExerciseType.BICYCLING)).get(i).getDistance()));
                gymEntries.add(new BarEntry(i, Objects.requireNonNull(weeklyResult.get(ExerciseType.GYM)).get(i).getGymGroup()));
                pushupEntries.add(new BarEntry(i, Objects.requireNonNull(weeklyResult.get(ExerciseType.PUSHUP)).get(i).getRep()));

                runningDataSet.setValues(runningEntries);
                bikingDataSet.setValues(bikingEntries);
                gymDataSet.setValues(gymEntries);
                pushupDataSet.setValues(pushupEntries);

                runningBarData.removeDataSet(0);
                runningBarData.addDataSet(runningDataSet);
                bikingBarData.removeDataSet(0);
                bikingBarData.addDataSet(bikingDataSet);
                gymBarData.removeDataSet(0);
                gymBarData.addDataSet(gymDataSet);
                pushupBarData.removeDataSet(0);
                pushupBarData.addDataSet(pushupDataSet);

                runningBarChart.setData(runningBarData);
                bikingBarChart.setData(bikingBarData);
                gymBarChart.setData(gymBarData);
                pushupBarChart.setData(pushupBarData);

                runningBarChart.animateXY(300,300);
                bikingBarChart.animateXY(300,300);
                gymBarChart.animateXY(300,300);
                pushupBarChart.animateXY(300,300);
            }

            runScore.setText(Integer.toString(totalStep));
            bikeScore.setText(totalDistance.toString());
            gymScore.setText(Integer.toString(totalAttempts));
            pushupScore.setText(Integer.toString(totalReps));

        } else {
            Float totalStep = 0F;
            Float totalDistance = 0F;
            Float totalAttempts = 0F;
            Float totalReps = 0F;

            runningEntries.clear();
            bikingEntries.clear();
            gymEntries.clear();
            pushupEntries.clear();

            runScoreText.setText("CALO");
            bikeScoreText.setText("CALO");
            gymScoreText.setText("CALO");
            pushupScoreText.setText("CALO");


            runningEntries.clear();
            bikingEntries.clear();
            gymEntries.clear();
            pushupEntries.clear();

            for(int i=0; i<7; i++) {
                totalStep += Objects.requireNonNull(weeklyResult.get(ExerciseType.RUNNING)).get(i).getCalo();
                totalDistance += Objects.requireNonNull(weeklyResult.get(ExerciseType.BICYCLING)).get(i).getCalo();
                totalAttempts += Objects.requireNonNull(weeklyResult.get(ExerciseType.GYM)).get(i).getCalo();
                totalReps += Objects.requireNonNull(weeklyResult.get(ExerciseType.PUSHUP)).get(i).getCalo();

                runningEntries.add(new BarEntry(i, Objects.requireNonNull(weeklyResult.get(ExerciseType.RUNNING)).get(i).getCalo()));
                bikingEntries.add(new BarEntry(i, Objects.requireNonNull(weeklyResult.get(ExerciseType.BICYCLING)).get(i).getCalo()));
                gymEntries.add(new BarEntry(i, Objects.requireNonNull(weeklyResult.get(ExerciseType.GYM)).get(i).getCalo()));
                pushupEntries.add(new BarEntry(i, Objects.requireNonNull(weeklyResult.get(ExerciseType.PUSHUP)).get(i).getCalo()));

                runningDataSet.setValues(runningEntries);
                bikingDataSet.setValues(bikingEntries);
                gymDataSet.setValues(gymEntries);
                pushupDataSet.setValues(pushupEntries);

                runningBarData.removeDataSet(0);
                runningBarData.addDataSet(runningDataSet);
                bikingBarData.removeDataSet(0);
                bikingBarData.addDataSet(bikingDataSet);
                gymBarData.removeDataSet(0);
                gymBarData.addDataSet(gymDataSet);
                pushupBarData.removeDataSet(0);
                pushupBarData.addDataSet(pushupDataSet);

                runningBarChart.setData(runningBarData);
                bikingBarChart.setData(bikingBarData);
                gymBarChart.setData(gymBarData);
                pushupBarChart.setData(pushupBarData);

                runningBarChart.animateXY(300,300);
                bikingBarChart.animateXY(300,300);
                gymBarChart.animateXY(300,300);
                pushupBarChart.animateXY(300,300);
            }

            runScore.setText(totalStep.toString());
            bikeScore.setText(totalDistance.toString());
            gymScore.setText(totalAttempts.toString());
            pushupScore.setText(totalReps.toString());
        }
    }
}
