package com.example.vital_hub.home_page;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vital_hub.R;

public class AddPostActivity extends AppCompatActivity {
    AutoCompleteTextView exerciseChoose;

    String[] item = {"Gym", "Pushup", "Run", "Cycle"};

    ArrayAdapter<String> adapterItems;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_exercise_choose);

        exerciseChoose = findViewById(R.id.exercise_dropdown);
        adapterItems = new ArrayAdapter<String>(this, R.layout.exercise_item, item);

        exerciseChoose.setAdapter(adapterItems);

        exerciseChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(AddPostActivity.this, "item " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
