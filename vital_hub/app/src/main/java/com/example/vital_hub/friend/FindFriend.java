package com.example.vital_hub.friend;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import com.example.vital_hub.R;

public class FindFriend extends AppCompatActivity {

    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> {
            finish();
        });
    }
}
