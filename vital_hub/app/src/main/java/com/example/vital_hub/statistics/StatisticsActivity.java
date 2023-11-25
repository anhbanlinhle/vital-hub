package com.example.vital_hub.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.home_page.AddPostActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.home_page.HpRecyclerAdapter;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    BarChart runningBarChart;
    BarChart bikingBarChart;
    BarChart gymBarChart;
    BarChart pushupBarChart;
    ImageView runningIcon;
    ImageView bikeIcon;
    ImageView gymIcon;
    ImageView pushupIcon;
    List<String> xValues = Arrays.asList("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

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

        int bikeColor = Color.parseColor("#00c9bd");
        int gymColor = Color.parseColor("#ec9a3a");
        int pushupColor = Color.parseColor("#e05252");

        bikeIcon.setColorFilter(bikeColor);
        gymIcon.setColorFilter(gymColor);
        pushupIcon.setColorFilter(pushupColor);

        ArrayList<BarEntry> runningEntries = new ArrayList<>();
        runningEntries.add(new BarEntry(0, 1));
        runningEntries.add(new BarEntry(1, 3));
        runningEntries.add(new BarEntry(2, 5));
        runningEntries.add(new BarEntry(3, 7));
        runningEntries.add(new BarEntry(4, 2));
        runningEntries.add(new BarEntry(5, 6));
        runningEntries.add(new BarEntry(6, 4));

        ArrayList<BarEntry> bikingEntries = new ArrayList<>();
        bikingEntries.add(new BarEntry(0, 1));
        bikingEntries.add(new BarEntry(1, 3));
        bikingEntries.add(new BarEntry(2, 5));
        bikingEntries.add(new BarEntry(3, 7));
        bikingEntries.add(new BarEntry(4, 2));
        bikingEntries.add(new BarEntry(5, 6));
        bikingEntries.add(new BarEntry(6, 4));

        ArrayList<BarEntry> gymEntries = new ArrayList<>();
        gymEntries.add(new BarEntry(0, 1));
        gymEntries.add(new BarEntry(1, 3));
        gymEntries.add(new BarEntry(2, 5));
        gymEntries.add(new BarEntry(3, 7));
        gymEntries.add(new BarEntry(4, 2));
        gymEntries.add(new BarEntry(5, 6));
        gymEntries.add(new BarEntry(6, 4));

        ArrayList<BarEntry> pushupEntries = new ArrayList<>();
        pushupEntries.add(new BarEntry(0, 1));
        pushupEntries.add(new BarEntry(1, 3));
        pushupEntries.add(new BarEntry(2, 5));
        pushupEntries.add(new BarEntry(3, 7));
        pushupEntries.add(new BarEntry(4, 2));
        pushupEntries.add(new BarEntry(5, 6));
        pushupEntries.add(new BarEntry(6, 4));

        BarDataSet runningDataSet = new BarDataSet(runningEntries, "RUNNING");
        runningDataSet.setColors(Color.parseColor("#1DB954"));

        BarDataSet bikingDataSet = new BarDataSet(bikingEntries, "BIKE");
        bikingDataSet.setColors(Color.parseColor("#00c9bd"));

        BarDataSet gymDataSet = new BarDataSet(gymEntries, "GYM");
        gymDataSet.setColors(Color.parseColor("#ec9a3a"));

        BarDataSet pushupDataSet = new BarDataSet(pushupEntries, "PUSHUP");
        pushupDataSet.setColors(Color.parseColor("#e05252"));


        BarData runningBarData = new BarData(runningDataSet);
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
        runningBarChart.animate();
        runningBarChart.invalidate();

        BarData gymBarData = new BarData(gymDataSet);
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
        gymBarChart.invalidate();

        BarData bikingBarData = new BarData(bikingDataSet);
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
        bikingBarChart.invalidate();

        BarData pushupBarData = new BarData(pushupDataSet);
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
        pushupBarChart.invalidate();
    }
}
