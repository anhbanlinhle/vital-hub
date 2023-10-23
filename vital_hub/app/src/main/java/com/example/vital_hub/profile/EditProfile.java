package com.example.vital_hub.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vital_hub.R;

public class EditProfile extends AppCompatActivity {

    Toolbar toolbar;

    TextView cancel;
    EditText userName;
    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.edit_profile);

        userName = (EditText)findViewById(R.id.username);
        cancel = findViewById(R.id.cancel);
        userName.setText("Username", TextView.BufferType.EDITABLE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
