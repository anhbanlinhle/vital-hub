package com.example.vital_hub.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestVideo extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_VIDEO = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 2;
    private VideoView videoView;
    Button video, back, upload;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_video);

        video = findViewById(R.id.choose);
        back = findViewById(R.id.back);
        upload = findViewById(R.id.upload);
        result = findViewById(R.id.result);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected video file
                Uri videoUri = videoView.getTag() != null ? (Uri) videoView.getTag() : null;
                if (videoUri == null) {
                    Toast.makeText(TestVideo.this, "No video selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                Gson gson = new Gson();
                // Create a Retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.7:7979") // Replace with your server's base URL
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                // Create a file object from the video URI
                File videoFile = new File(getVideoPathFromUri(videoUri));

                // Create a request body from the video file
                RequestBody videoRequestBody = RequestBody.create(MediaType.parse("video/*"), videoFile);

                // Create a multipart request body with the video file and a filename
                MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video_data", videoFile.getName(), videoRequestBody);

                // Create a Retrofit service for sending the video
                TestVideoService videoService = retrofit.create(TestVideoService.class);

                // Send the video to the server
                Call<TestVideoService.ServerResponse> call = videoService.sendVideo(videoPart);
                call.clone().enqueue(new Callback<TestVideoService.ServerResponse>() {
                    @Override
                    public void onResponse(Call<TestVideoService.ServerResponse> call, Response<TestVideoService.ServerResponse> response) {
                        if (response.isSuccessful()) {
                            TestVideoService.ServerResponse serverResponse = response.body();
                            if (serverResponse != null) {
                                result.setText("Count: " + serverResponse.getCount());
                            } else {
                                result.setText("Invalid response from server");
                            }
                        } else {
                            Toast.makeText(TestVideo.this, "Failed to send video", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TestVideoService.ServerResponse> call, Throwable t) {
                        result.setText(t.getMessage());
                    }
                });
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
            videoView.setTag(videoUri);

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