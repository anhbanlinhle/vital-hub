package com.example.vital_hub.home_page;

import static com.example.vital_hub.client.spring.controller.Api.initAddPost;
import static com.example.vital_hub.client.spring.controller.Api.initGetExerciseList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.ExerciseResponse;
import com.example.vital_hub.competition.data.CompetitionAdd;
import com.example.vital_hub.helper.ImgToUrl.ImageUploadTask;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {
    Spinner exerciseChoose;

    ArrayList<ExerciseResponse> arrayList;

    ExSpinnerAdapter adapter;

    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;

    List<ExerciseResponse> exerciseResponseList;
    Button img_add_btn;
    ShapeableImageView add_post_img;
    public static final int PICK_IMAGE = 1;
    HomePagePost body;
    Long exercise_id;
    ImageButton submit_btn;
    TextInputLayout post_content;
    Boolean hasPickedImage = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_exercise_choose);

        exerciseChoose = findViewById(R.id.ex_list_spinner);
        img_add_btn = findViewById(R.id.add_post_img_btn);
        submit_btn = findViewById(R.id.submit_post_button);
        post_content = findViewById(R.id.add_post_input);

        arrayList = new ArrayList<>();
        initHeaderForRequest();
        fetchExercise(0);


        adapter = new ExSpinnerAdapter(this, arrayList);
        exerciseChoose.setAdapter(adapter);

        img_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasPickedImage) {
                    ExerciseResponse exerciseResponse = (ExerciseResponse) exerciseChoose.getSelectedItem();
                    exercise_id = exerciseResponse.getExerciseId();
                    uploadImageAndSetUrl(post_content.getEditText().getText().toString().trim(), exercise_id);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE) {
                Uri selectedImage = data.getData();
                add_post_img = findViewById(R.id.add_post_img);
                add_post_img.setImageURI(selectedImage);
                hasPickedImage = true;
            }
        }
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchExercise(int pageNum) {
        initGetExerciseList(headers, pageNum);
        Api.getExerciseList.clone().enqueue(new Callback<List<ExerciseResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ExerciseResponse>> call, @NonNull Response<List<ExerciseResponse>> response) {
                exerciseResponseList = response.body();
                assert exerciseResponseList != null;
                arrayList.addAll(exerciseResponseList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ExerciseResponse>> call, @NonNull Throwable t) {
                Log.e("Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    //add post
    private void addPost() {
        initAddPost(headers, body);
        Api.addPost.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200) {
                    Toast.makeText(AddPostActivity.this, "Error occured. Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(AddPostActivity.this, "Add post successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddPostActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImageAndSetUrl(String content, Long exercise_id) {
        ImageUploadTask imageUploadTask = new ImageUploadTask(add_post_img, new ImageUploadTask.ImageUploadCallback() {
            @Override
            public void onImageUploaded(String imageUrl) {
                body = new HomePagePost();
                body.setImage(imageUrl);
                body.setTitle(content);
                body.setExerciseId(exercise_id);
                addPost();
            }

            @Override
            public void onUploadFailed() {
                Toast.makeText(AddPostActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
        imageUploadTask.execute();

    }
}
