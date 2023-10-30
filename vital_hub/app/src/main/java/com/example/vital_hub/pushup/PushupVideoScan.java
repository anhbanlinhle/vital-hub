package com.example.vital_hub.pushup;

import static com.example.vital_hub.client.fastapi.controller.VideoApi.initFastapi;
import static com.example.vital_hub.client.fastapi.controller.VideoApi.initPushupCall;
import static com.example.vital_hub.client.fastapi.controller.VideoApi.pushupCall;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.fastapi.objects.PushUpResponse;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.exercises.ExerciseGeneralActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.profile.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushupVideoScan extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private RecyclerView resultRecycler;
    private ArrayList<Integer> arrayList;
    PushupAdapter recyclerAdapter;

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 0;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_VIDEO = 2;
    VideoView videoView;
    FloatingActionButton chooseVideo, uploadVideo, save;
    TextView back, help;
    BottomNavigationView bottomNavigationView;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushup_video_scan);

        arrayList = new ArrayList<>();
        resultRecycler = findViewById(R.id.resultRecycle);

        recyclerAdapter = new PushupAdapter(arrayList);
        resultRecycler.setAdapter(recyclerAdapter);
        resultRecycler.setLayoutManager(new LinearLayoutManager(this));

        videoView = findViewById(R.id.video_view);
        chooseVideo = findViewById(R.id.chooseVideo);
        uploadVideo = findViewById(R.id.uploadVideo);
        save = findViewById(R.id.save);

        back = findViewById(R.id.back_to_home_from_pushup);
        help = findViewById(R.id.help);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.exercise);

        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        initFastapi(prefs.getString("server", "10.0.2.2"));

        checkSelfPermission();

        configVideoView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PushupVideoScan.this, ExerciseGeneralActivity.class));
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PushupVideoScan.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });
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
        arrayList.clear();
        arrayList.add(null);
        recyclerAdapter.notifyItemRangeChanged(0, 1);

        String videoPath = getVideoPathFromUri(videoUri);
        initPushupCall(videoPath);

        pushupCall.clone().enqueue(new Callback<PushUpResponse>() {
            @Override
            public void onResponse(Call<PushUpResponse> call, Response<PushUpResponse> response) {
                if (response.isSuccessful()) {
                    PushUpResponse body = response.body();
                    if (body != null) {
                        arrayList.remove(0);
                        arrayList.add(body.getCount());
                        recyclerAdapter.notifyItemRangeChanged(0, 1);
//                        result.setText(String.valueOf(body.getCount()));
                    }
                }
            }

            @Override
            public void onFailure(Call<PushUpResponse> call, Throwable t) {
//                result.setText(t.getMessage());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), UserProfile.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.home) {
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.exercise) {
            return true;
        } else if (item.getItemId() == R.id.competition) {
            startActivity(new Intent(getApplicationContext(), CompetitionActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else {
            return false;
        }
    }
}