package com.example.vital_hub.home_page;

import static com.example.vital_hub.client.spring.controller.Api.initAddPost;
import static com.example.vital_hub.client.spring.controller.Api.initGetExerciseList;
import static com.example.vital_hub.client.spring.controller.Api.initGetPostResponse;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.ExerciseResponse;
import com.example.vital_hub.post_comment.PostCommentActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {
    AutoCompleteTextView exerciseChoose;

    String[] item = {"Gym", "Pushup", "Run", "Cycle"};

    ArrayAdapter<String> adapterItems;

    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;

    List<ExerciseResponse> exerciseResponseList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_exercise_choose);

        exerciseChoose = findViewById(R.id.exercise_dropdown);
        adapterItems = new ArrayAdapter<>(this, R.layout.exercise_item, item);

        exerciseChoose.setAdapter(adapterItems);

        exerciseChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(AddPostActivity.this, "item " + item, Toast.LENGTH_SHORT).show();
            }
        });
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
            public void onResponse(Call<List<ExerciseResponse>> call, Response<List<ExerciseResponse>> response) {
                exerciseResponseList = response.body();
            }

            @Override
            public void onFailure(Call<List<ExerciseResponse>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    //add post
    private void addPost() {
        HomePagePost body = new HomePagePost("asd");
        initAddPost(headers, body);
        Api.addPost.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200) {
                    Toast.makeText(AddPostActivity.this, "Error occured. Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                // TO-DO: response successful
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddPostActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
