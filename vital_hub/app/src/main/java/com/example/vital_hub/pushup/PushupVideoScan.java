package com.example.vital_hub.pushup;

import static com.example.vital_hub.client.fastapi.controller.VideoApi.initFastapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.vital_hub.R;

public class PushupVideoScan extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_VIDEO = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 2;
    VideoView videoView;
    Button video, back, upload;
    TextView result;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushup_video_scan);

        videoView = findViewById(R.id.video_view);
        video = findViewById(R.id.choose);
        upload = findViewById(R.id.upload);
        back = findViewById(R.id.back);
        result = findViewById(R.id.result);

        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        initFastapi(prefs.getString("server", "10.0.2.2"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}