package com.example.vital_hub.pushup;

import static com.example.vital_hub.client.fastapi.controller.VideoApi.initFastapi;
import static com.example.vital_hub.client.fastapi.controller.VideoApi.initPushupCall;
import static com.example.vital_hub.client.fastapi.controller.VideoApi.pushupCall;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.fastapi.objects.PushUpResponse;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CompetitionMinDetailResponse;
import com.example.vital_hub.client.spring.objects.SaveExerciseAndCompetitionDto;
import com.example.vital_hub.competition.data.CompetitionMinDetail;
import com.example.vital_hub.utils.ExerciseType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushupVideoScan extends AppCompatActivity {
    // PushUp count display
    private RecyclerView resultRecycler;
    private ArrayList<Integer> arrayList;
    PushupAdapter recyclerAdapter;
    // Competition selector display
    List<String> items = new ArrayList<>(Collections.singletonList("None"));
    private AutoCompleteTextView competitionTitle;
    private ArrayAdapter<String> compeAdapter;
    List<CompetitionMinDetail> competitionMinDetails;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 0;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_VIDEO = 2;

    private Long competitionId;

    private SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto;
    VideoView videoView;
    AppCompatButton back;
    TextView pose, video, save, submit;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    FloatingActionButton scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushup_video_scan);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.color_green));

        arrayList = new ArrayList<>();
        resultRecycler = findViewById(R.id.resultRecycle);

        recyclerAdapter = new PushupAdapter(arrayList);
        resultRecycler.setAdapter(recyclerAdapter);
        resultRecycler.setLayoutManager(new LinearLayoutManager(this));

        back = findViewById(R.id.back);
        competitionTitle = findViewById(R.id.auto_complete_txt);
        videoView = findViewById(R.id.video_view);

        pose = findViewById(R.id.pose);
        video = findViewById(R.id.video);
        save = findViewById(R.id.save);
        submit = findViewById(R.id.submit);
        scan = findViewById(R.id.scan);

        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        initFastapi(prefs.getString("server", "10.0.2.2"));

        checkSelfPermission();

        initHeaderForRequest();
        configCompetitionSelector();
        configVideoView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(PushupVideoScan.this, ExerciseGeneralActivity.class));
                finish();
            }
        });
        pose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PushupVideoScan.this, PushUpPoseActivity.class));
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processVideo();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveExerciseAndCompetitionDto == null) {
                    openPopup("Alert", "No result to be saved", Styles.ALERT);
                } else {
                    handleSaveExercise();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveExerciseAndCompetitionDto == null) {
                    openPopup("Alert", "No result to be saved", Styles.ALERT);
                } else {
                    if (saveExerciseAndCompetitionDto.getCompetitionId() == null) {
                        openPopup("Alert", "Pick one competition to submit", Styles.ALERT);
                    } else {
                        handleSaveCompetition();
                    }
                }
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
//            Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show();
            openPopup("Alert", "No video selected", Styles.ALERT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read permission granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                openPopup("Alert", "Permission denied", Styles.ALERT);
            }
        }
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Write permission granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                openPopup("Alert", "Permission denied", Styles.ALERT);
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

    void configCompetitionSelector() {
        getCompetitionTitleList();
        compeAdapter = new ArrayAdapter<>(PushupVideoScan.this, android.R.layout.simple_list_item_1, items);
        competitionTitle.setAdapter(compeAdapter);
        competitionTitle.setDropDownHeight(compeAdapter.getCount() > 3 ? 450 : compeAdapter.getCount() * 150);
        competitionTitle.setText(items.get(0), false);
        compeTitleOnClick();
    }
    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }
    private void getCompetitionTitleList() {
        try {
            Api.initGetJoinedCompetitionPushUp(headers);
            Api.getJoinedCompetitionPushUp.clone().enqueue(new Callback<CompetitionMinDetailResponse>() {
                @Override
                public void onResponse(@NonNull Call<CompetitionMinDetailResponse> call, @NonNull Response<CompetitionMinDetailResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            competitionMinDetails = response.body().getData();
                            if (competitionMinDetails != null) {

                                for (int i = 0; i < competitionMinDetails.size(); i++) {
                                    items.add(competitionMinDetails.get(i).getTitle() + "  #" + competitionMinDetails.get(i).getId());
                                }
                                compeAdapter.notifyDataSetChanged();
                                competitionTitle.setDropDownHeight(compeAdapter.getCount() > 3 ? 450 : compeAdapter.getCount() * 150);
                            }
                        }
                    } else {
                        openPopup("Oh no", "Error loading competition: " + response.message(), Styles.FAILED);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CompetitionMinDetailResponse> call, @NonNull Throwable t) {
                    openPopup("Oh no", "Error loading competition: " + t.getMessage(), Styles.FAILED);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void compeTitleOnClick() {
        competitionTitle.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            competitionTitle.setText(selected, false);
            if (position == 0) {
                return;
            }

            int splitPos = 0;
            for (int i = selected.length() - 1; i >= 0; i--) {
                if (selected.charAt(i) == '#') {
                    splitPos = i;
                    break;
                }
            }
            competitionId = Long.parseLong(selected.substring(splitPos + 1));
            if (saveExerciseAndCompetitionDto != null) {
                saveExerciseAndCompetitionDto.setCompetitionId(competitionId);
            }
            if (position != 0) {
//                isCompeting = true;
//                isRunningCompetition = false;
                competitionTitle.clearFocus();
//                competingNotification();
//                handleIfCompetition();
            } else {
//                handleIfNone();
            }
        });
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
            openPopup("Alert", "No video selected", Styles.ALERT);
            return;
        }
        arrayList.clear();
        arrayList.add(null);
        recyclerAdapter.notifyItemRangeChanged(0, 1);

        String videoPath = getVideoPathFromUri(videoUri);
        Log.i("videoPath", videoPath);
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

                        initExerciseAndCompetitionDto(body.getCount());
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

    private void initExerciseAndCompetitionDto(int rep) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        saveExerciseAndCompetitionDto = new SaveExerciseAndCompetitionDto();
        saveExerciseAndCompetitionDto.setCompetitionId(competitionId);
        saveExerciseAndCompetitionDto.setCalo(0.5F * rep);
        saveExerciseAndCompetitionDto.setType(ExerciseType.PUSHUP);
        saveExerciseAndCompetitionDto.setRep(rep);
        saveExerciseAndCompetitionDto.setStartedAt(LocalDateTime.now().format(formatter));
    }

    private void handleSaveExercise() {
        Api.saveExercise(headers, saveExerciseAndCompetitionDto);

        Api.savedExercise.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    openPopup("Well done", "Save push up count successful", Styles.SUCCESS);
                } else {
                    openPopup("Oh no", "Could not save push up count", Styles.FAILED);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                openPopup("Oh no", "Could not save push up count", Styles.FAILED);
            }
        });
    }

    private void handleSaveCompetition() {
        Api.saveResultForCompetition(headers, saveExerciseAndCompetitionDto);

        Api.savedCompetitionResult.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    openPopup("Well done", "Save result for competition successful", Styles.SUCCESS);
                } else {
                    openPopup("Oh no", "Could not save result for competition", Styles.FAILED);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                openPopup("Oh no", "Could not save result for competition", Styles.FAILED);
            }
        });
    }

    private void openPopup(String heading, String description, Styles styles) {
        PopupDialog.getInstance(this)
            .setStyle(styles)
            .setHeading(heading)
            .setDescription(description)
            .setCancelable(true)
            .showDialog(new OnDialogButtonClickListener() {
                @Override
                public void onDismissClicked(Dialog dialog) {
                    super.onDismissClicked(dialog);
                }
            });
    }
}