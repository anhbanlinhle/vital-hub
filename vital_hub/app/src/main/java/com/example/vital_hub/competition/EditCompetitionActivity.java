package com.example.vital_hub.competition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.competition.data.CompetitionEdit;
import com.example.vital_hub.utils.HeaderInitUtil;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCompetitionActivity extends AppCompatActivity {

    private EditText startAt;

    private EditText endAt;

    private EditText title;

    private EditText duration;

    private Button saveBtn;

    private Map<String, String> header;

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
        saveBtn = findViewById(R.id.compe_edit_save_btn);
        header = HeaderInitUtil.headerWithToken(this);

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

        buttonBinding();
    }

    private void buttonBinding() {
        this.saveBtn.setOnClickListener(v -> {
            CompetitionEdit competitionEdit = new CompetitionEdit();
            competitionEdit.setId(getIntent().getLongExtra("id", -1));
            competitionEdit.setDuration(duration.getText().toString());
            competitionEdit.setTitle(title.getText().toString());
            competitionEdit.setEndedAt(endAt.getText().toString());
            competitionEdit.setStartedAt(startAt.getText().toString());

            callEditApi(competitionEdit);
        });
    }

    private void callEditApi(CompetitionEdit competitionEdit) {
        Api.initEditCompetition(header, competitionEdit);

        Api.editCompetition.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditCompetitionActivity.this, "Edit competition successfully", Toast.LENGTH_SHORT).show();
                    waitStartActivity(CompetitionDetailActivity.class);

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditCompetitionActivity.this, "Fail to edit competition", Toast.LENGTH_SHORT).show();
            }
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

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

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

                        String selectedDuration = (hourOfDay < 10 ? ("0" + hourOfDay) : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute);
                        duration.setText(selectedDuration);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void waitStartActivity(Class<?> cls) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(EditCompetitionActivity.this, cls));
            }
        }, 2000);
    }
}