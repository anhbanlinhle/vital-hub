package com.example.vital_hub.competition;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.vital_hub.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddCompeActivity extends AppCompatActivity {

    private ImageButton btnRun;

    private ImageButton btnBicyle;

    private TextView compeTypeView;

    private EditText startAt;

    private EditText endAt;

    private String compeType;

    private Button addImgBtn;
    private AppCompatButton addButton;
    private ImageView imageView;

    private final int addImgCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compe);

        initDeclaration();
    }

    private void initDeclaration() {

        compeType = "";
        compeTypeView = findViewById(R.id.compe_type);
        btnRun = findViewById(R.id.btn_run);
        btnBicyle = findViewById(R.id.btn_bicycle);
        startAt = findViewById(R.id.compe_start);
        endAt = findViewById(R.id.compe_end);
        addImgBtn = findViewById(R.id.add_compe_img_btn);
        imageView = findViewById(R.id.compe_img_holder);
        addButton = findViewById(R.id.add);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            buttonBinding();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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

        addImgBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            startActivityForResult(intent, addImgCode);
        });

        startAt.setOnClickListener(v -> {
            showDateTimeDialog(startAt);
        });

        endAt.setOnClickListener(v -> {
            showDateTimeDialog(endAt);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                imageView.setImageURI(uri);
            }
        }
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

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);

                        dateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(AddCompeActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        };
        new DatePickerDialog(AddCompeActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}