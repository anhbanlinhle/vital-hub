package com.example.vital_hub.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vital_hub.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {

    ImageView profileImage;
    Calendar calendar;
    Toolbar toolbar;
    EditText birthDate;
    TextView cancel;
    EditText userName;

    BottomNavigationView navBar;
    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.edit_profile);

        userName = findViewById(R.id.edit_username);
        birthDate = findViewById(R.id.edit_birthdate);
        cancel = findViewById(R.id.cancel);
        calendar = Calendar.getInstance();
        profileImage = findViewById(R.id.profile_image);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditProfile.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//        birthDate.setText("Your DOB", TextView.BufferType.EDITABLE);
//        userName.setText("Username", TextView.BufferType.EDITABLE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(EditProfile.this, profileImage);
                popupMenu.getMenuInflater().inflate(R.menu.profile_image_options, popupMenu.getMenu());
                popupMenu.show();
            }
        });

    }

    private void updateLabel() {
        String myFormat="mm/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        birthDate.setText(dateFormat.format(calendar.getTime()));
    }

}
