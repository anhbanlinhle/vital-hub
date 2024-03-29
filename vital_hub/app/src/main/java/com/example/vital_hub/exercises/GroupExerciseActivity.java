package com.example.vital_hub.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.OriginExercise;
import com.example.vital_hub.client.spring.objects.SaveExerciseAndCompetitionDto;
import com.example.vital_hub.exercises.adapter.SingleExerciseAdapter;
import com.example.vital_hub.exercises.data_container.SingleExercise;
import com.example.vital_hub.utils.ExerciseType;
import com.example.vital_hub.utils.HeaderInitUtil;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupExerciseActivity extends AppCompatActivity {

    private RecyclerView geRecycler;
    private SingleExerciseAdapter singleExerciseAdapter;
    private List<SingleExercise> seList;

    private Long groupId;

    private Map<String, String> header;

    private TextView title;
    private TextView back;

    private ImageButton submitBtn;

    private Boolean startWorkingOut;

    private SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto;

    private Float totalCalo;

    private NotificationManagerCompat notificationManager;

    private NotificationCompat.Builder builder;

    private final Integer CHANNEL_ID = 2812;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_exercise);

        dataInit();
    }

    private void dataInit() {
        startWorkingOut = false;
        saveExerciseAndCompetitionDto = new SaveExerciseAndCompetitionDto();
        totalCalo = 0F;
        seList = new ArrayList<>();
        groupId = getIntent().getLongExtra("group_id", 0);
        back = (TextView) findViewById(R.id.back_to_choose_ex);
        title = (TextView) findViewById(R.id.ge_ac_title);
        title.setText("Group " + groupId);
        submitBtn = findViewById(R.id.submit_ex_btn);
        header = HeaderInitUtil.headerWithToken(this);
        geRecycler = (RecyclerView) findViewById(R.id.ge_ac_recycler);
        builder = new NotificationCompat.Builder(this, String.valueOf(CHANNEL_ID))
                .setSmallIcon(R.drawable.noti_icon)
                .setContentTitle("Notification")
                .setContentText("Working out on exercises in group " + groupId)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager = NotificationManagerCompat.from(this);

        buttonBinding();

        fetchExerciseInGroup();
    }

    private void buttonBinding() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submitBtn.setOnClickListener(v -> {
            startWorkingOut = !startWorkingOut;
            if (startWorkingOut) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                saveExerciseAndCompetitionDto.setStartedAt(LocalDateTime.now().format(formatter));
                saveExerciseAndCompetitionDto.setType(ExerciseType.GYM);
                saveExerciseAndCompetitionDto.setCalo(totalCalo);
                saveExerciseAndCompetitionDto.setGroupId(groupId);

                submitBtn.setBackgroundResource(R.drawable.rounded_button_red);
                submitBtn.setImageResource(R.drawable.baseline_av_timer_32_white);

                showNotification();
            } else {
                Api.saveExercise(header, saveExerciseAndCompetitionDto);

                Api.savedExercise.clone().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            openPopup("Well done", "Save attempt successfully", Styles.SUCCESS);
                            saveExerciseAndCompetitionDto = new SaveExerciseAndCompetitionDto();
                            submitBtn.setBackgroundResource(R.drawable.rounded_button_green);
                            submitBtn.setImageResource(R.drawable.baseline_cloud_upload_32_white);
                            notificationManager.cancel(810);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        openPopup("Oh no", "Fail to save exercise", Styles.FAILED);
                    }
                });
            }
        });
    }

    private void showNotification() {
        String channelId = "running_channel";
        NotificationChannel channel = new NotificationChannel(channelId, "Running Channel", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        builder.setChannelId(channelId);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1111);
        }
        notificationManager.notify(810, builder.build());
    }

    private void fetchExerciseInGroup() {
        Api.getGroupExerciseById(header, groupId);

        Api.exercisesInGroup.clone().enqueue(new Callback<List<SingleExercise>>() {
            @Override
            public void onResponse(Call<List<SingleExercise>> call, Response<List<SingleExercise>> response) {
                if (response.isSuccessful()) {
                    seList = response.body();

                    seList.forEach((ex) -> {
                        totalCalo += ex.getTotalCalo();
                    });

                    singleExerciseAdapter = new SingleExerciseAdapter(seList);
                    geRecycler.setAdapter(singleExerciseAdapter);
                    geRecycler.setLayoutManager(new LinearLayoutManager(GroupExerciseActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<SingleExercise>> call, Throwable t) {
                Toast.makeText(GroupExerciseActivity.this, "Fail to get exercise in group " + groupId, Toast.LENGTH_SHORT).show();
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