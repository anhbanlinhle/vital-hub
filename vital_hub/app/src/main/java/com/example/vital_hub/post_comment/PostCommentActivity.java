package com.example.vital_hub.post_comment;

import static com.example.vital_hub.client.controller.Api.initPostComment;
import static com.example.vital_hub.client.controller.Api.postComment;
import static com.example.vital_hub.client.controller.Api.postRegist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.authentication.FirstRegistInfo;
import com.example.vital_hub.client.controller.Api;
import com.example.vital_hub.client.objects.CommentPost;
import com.example.vital_hub.home_page.HomePagePost;
import com.example.vital_hub.test.TestMain;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCommentActivity extends AppCompatActivity {
    private RecyclerView cmtRecycler;
    private ArrayList<Comment> arrayList;
    private boolean isLoading = false;
    CommentRecyclerAdapter recyclerAdapter;

    private ImageButton back_button;
    private ImageButton send_button;

    private TextInputLayout comment_input;
    private Long postId;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    public static List<Comment> commentResponse;
    private int pageNum = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_comment_layout);
        postId = this.getIntent().getLongExtra("postId", -1);
        initHeaderForRequest();
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> finish());
        send_button = findViewById(R.id.send_button);
        comment_input = findViewById(R.id.comment_input);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentPost body = new CommentPost(postId, comment_input.getEditText().getText().toString().trim());
                initPostComment(headers, body);
                postComment.clone().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() != 200) {
                            Toast.makeText(PostCommentActivity.this, "Error occured. Code: " + response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Comment comment = new Comment(R.drawable.ic_launcher_background, "Profile Name" , comment_input.getEditText().getText().toString().trim());
                        arrayList.add(1, comment);
                        recyclerAdapter.notifyItemRangeChanged(arrayList.size()-1, 1);
                        comment_input.getEditText().getText().clear();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(PostCommentActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        arrayList = new ArrayList<>();

        cmtRecycler = findViewById(R.id.comment_recycler);

        arrayList.add(new Comment(new HomePagePost(R.drawable.ic_launcher_background, R.drawable.app_icon, "title", "message")));

        populateData();

        recyclerAdapter = new CommentRecyclerAdapter(arrayList);
        cmtRecycler.setAdapter(recyclerAdapter);
        cmtRecycler.setLayoutManager(new LinearLayoutManager(this));

        cmtRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) cmtRecycler.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size() - 1) {
                        isLoading = true;
                        getMoreData();
                    }
                }
            }
        });
    }

    private void populateData() {
        fetchComment(pageNum, postId);
        pageNum++;
    }

    private void getMoreData() {
        // ADD DATA FROM DB
        arrayList.remove(arrayList.size() - 1);
        fetchComment(pageNum, postId);
        pageNum++;
        isLoading = false;
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchComment(int pageNum, Long postId) {
        Api.initGetCommentResponse(headers, pageNum, postId);
        Api.getCommentResponse.clone().enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    commentResponse = response.body();
                    for(Comment comment : commentResponse) {
                        comment.setProfileName(String.valueOf(arrayList.size()));
                        arrayList.add(comment);
                    }
                    recyclerAdapter.notifyItemRangeChanged(arrayList.size() - commentResponse.size(), commentResponse.size());
                    arrayList.add(null);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}
