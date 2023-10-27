package com.example.vital_hub.pushup;

import static com.example.vital_hub.client.fastapi.controller.VideoApi.initFastapi;
import static com.example.vital_hub.client.fastapi.controller.VideoApi.initPushupCall;
import static com.example.vital_hub.client.fastapi.controller.VideoApi.pushupCall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.fastapi.objects.PushUpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushupVideoScan extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 0;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_VIDEO = 2;
    VideoView videoView;
    FloatingActionButton chooseVideo, uploadVideo;
    TextView result;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushup_video_scan);

        videoView = findViewById(R.id.video_view);
        result = findViewById(R.id.result);
        chooseVideo = findViewById(R.id.chooseVideo);
        uploadVideo = findViewById(R.id.uploadVideo);

        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        initFastapi(prefs.getString("server", "10.0.2.2"));

        checkSelfPermission();

        configVideoView();

        chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processVideo();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri videoUri = data.getData();

            videoView.setVideoURI(videoUri);
            videoView.setTag(videoUri);
            videoView.start();
        } else {
            Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read permisson granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Write permisson granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(
                PushupVideoScan.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PushupVideoScan.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(
                PushupVideoScan.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission to read external storage
            ActivityCompat.requestPermissions(PushupVideoScan.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    void configVideoView() {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }

    void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
    }

    void processVideo() {
        Uri videoUri = videoView.getTag() != null ? (Uri) videoView.getTag() : null;
        if (videoUri == null) {
            Toast.makeText(PushupVideoScan.this, "No video selected", Toast.LENGTH_SHORT).show();
            return;
        }
        result.setText("Processing... Please wait!");

        String videoPath = getVideoPathFromUri(videoUri);
        initPushupCall(videoPath);

        pushupCall.clone().enqueue(new Callback<PushUpResponse>() {
            @Override
            public void onResponse(Call<PushUpResponse> call, Response<PushUpResponse> response) {
                if (response.isSuccessful()) {
                    PushUpResponse body = response.body();
                    if (body != null) {
                        result.setText("Count: " + body.getCount());
                    }
                }
            }

            @Override
            public void onFailure(Call<PushUpResponse> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

    public String getVideoPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }
}