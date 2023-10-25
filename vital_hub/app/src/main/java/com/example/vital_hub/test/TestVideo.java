package com.example.vital_hub.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vital_hub.R;

public class TestVideo extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_VIDEO = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 2;
    private VideoView videoView;
    Button video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_video);

        video = findViewById(R.id.choose);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the video selection intent
                if (ContextCompat.checkSelfPermission(TestVideo.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to read external storage
                    ActivityCompat.requestPermissions(TestVideo.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    // Start the video selection intent
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                }
            }
        });

        videoView = findViewById(R.id.video_view);

        // Set a MediaController on the VideoView
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_VIDEO && resultCode == RESULT_OK && data != null) {
            // Get the selected video URI
            Uri videoUri = data.getData();

            // Set the video URI on the VideoView
            videoView.setVideoURI(videoUri);

            // Start playing the video
            videoView.start();
        } else {
            // Handle the case where the user didn't select a video
            Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the video selection intent
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}