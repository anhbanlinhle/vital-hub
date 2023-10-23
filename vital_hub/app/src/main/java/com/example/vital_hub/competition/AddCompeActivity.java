package com.example.vital_hub.competition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.vital_hub.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddCompeActivity extends AppCompatActivity {

    private ImageButton btnRun;

    private ImageButton btnBicyle;

    private TextView compeTypeView;

    private EditText startAt;

    private String compeType;

    private View dialogView;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compe);

        initDeclaration();
    }

    private void initDeclaration() {
        dialogView = View.inflate(this, R.layout.date_time_picker, null);
        alertDialog = new AlertDialog.Builder(this).create();

        compeType = "";
        compeTypeView = findViewById(R.id.compe_type);
        btnRun = findViewById(R.id.btn_run);
        btnBicyle = findViewById(R.id.btn_bicycle);
        startAt = findViewById(R.id.compe_start);

        buttonBinding();
    }

    private void buttonBinding() {
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!compeType.equals(btnRun.getContentDescription().toString())) {
                    compeType = btnRun.getContentDescription().toString();
                    compeTypeView.setText(compeType);
                    btnRun.setImageResource(R.drawable.baseline_directions_run_24_white);
                    btnRun.setBackgroundResource(R.drawable.full_rounded_corner_green_bg);

                    btnBicyle.setBackgroundResource(R.drawable.full_rounded_corner_green_border);
                    btnBicyle.setImageResource(R.drawable.baseline_directions_bike_24_green);
                }
            }
        });

        btnBicyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!compeType.equals(btnBicyle.getContentDescription().toString())) {
                    compeType = btnBicyle.getContentDescription().toString();
                    compeTypeView.setText(compeType);
                    btnBicyle.setImageResource(R.drawable.baseline_directions_bike_24_white);
                    btnBicyle.setBackgroundResource(R.drawable.full_rounded_corner_green_bg);

                    btnRun.setBackgroundResource(R.drawable.full_rounded_corner_green_border);
                    btnRun.setImageResource(R.drawable.baseline_directions_run_24);
                }
            }
        });

        startAt.setOnClickListener(v -> {
            DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
            TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
            Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth(),
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute());

            alertDialog.dismiss();
            alertDialog.setView(dialogView);
            alertDialog.show();
        });

    }
}