package com.example.vital_hub.friend;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vital_hub.R;

import java.util.ArrayList;

public class FindUser extends AppCompatActivity {

    private Button returnButton;
    private EditText friendName;
    private ArrayList<Friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        // Return to previous activity
        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> {
            finish();
        });
        // Get friend list from FriendList activity
        friendList = (ArrayList<Friend>) getIntent().getSerializableExtra("friendList");
        Toast.makeText(this, "Friend list size: " + friendList.size(), Toast.LENGTH_SHORT).show();


    }
}
