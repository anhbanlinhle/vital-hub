package com.example.vital_hub.competition;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vital_hub.R;

public class AddCompeActivity extends AppCompatActivity {

    private ImageButton btnRun;

    private ImageButton btnBicyle;

    private TextView compeTypeView;

    private String compeType;

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
    }
}