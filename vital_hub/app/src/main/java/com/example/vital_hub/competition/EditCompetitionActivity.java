package com.example.vital_hub.competition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.vital_hub.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Locale;

public class EditCompetitionActivity extends AppCompatActivity {

    private EditText startAt;

    private EditText endAt;

    private EditText title;

    private EditText duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_competition);

        initDeclaration();
    }

    private void initDeclaration() {
        title = findViewById(R.id.compe_edit_title);
        startAt = findViewById(R.id.compe_edit_start);
        endAt = findViewById(R.id.compe_edit_end);
        duration = findViewById(R.id.compe_edit_duration);

        title.setText(getIntent().getStringExtra("title"));
        duration.setText(getIntent().getStringExtra("duration"));
        startAt.setText(getIntent().getStringExtra("startedAt"));
        endAt.setText(getIntent().getStringExtra("endedAt"));

        startAt.setOnClickListener(v -> {
            showDateTimeDialog(startAt);
        });

        endAt.setOnClickListener(v -> {
            showDateTimeDialog(endAt);
        });

        duration.setOnClickListener(v -> {
            showTimeDialog(duration);
        });
    }

    private void showDateTimeDialog(EditText dateTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

                        dateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(EditCompetitionActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        };
        new DatePickerDialog(EditCompetitionActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EditCompetitionActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);

                        String selectedDuration = hourOfDay + ":" + minute;
                        duration.setText(selectedDuration);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }
}