package com.example.vital_hub.post_comment;

import static com.example.vital_hub.client.spring.controller.Api.initPostComment;
import static com.example.vital_hub.client.spring.controller.Api.postComment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CommentPost;
import com.example.vital_hub.home_page.HomePagePost;
import com.google.android.material.snackbar.Snackbar;
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
    String type;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    public static List<Comment> commentResponse;
    private HomePagePost singlePost;
    private int pageNum = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_comment_layout);
        postId = this.getIntent().getLongExtra("postId", -1);
        type = this.getIntent().getStringExtra("type");

        initHeaderForRequest();
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> finish());
        send_button = findViewById(R.id.send_button);
        comment_input = findViewById(R.id.comment_input);

        arrayList = new ArrayList<>();

        cmtRecycler = findViewById(R.id.comment_recycler);

        fetchSinglePost(postId, type);

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
                        comment_input.getEditText().getText().clear();
                        refetchComment();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(PostCommentActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            case 101:
                recyclerAdapter.deleteComment(item.getGroupId(), PostCommentActivity.this);
                return true;
        }
        return false;
    }

    private void getMoreData() {
        arrayList.add(null);
        // ADD DATA FROM DB
        arrayList.remove(arrayList.size() - 1);
        fetchComment(pageNum, postId);
        pageNum++;
        isLoading = false;
    }

    private void refetchComment() {
        int size = arrayList.size();
        for(int i = 0; i < size - 1; i++) {
            arrayList.remove(1);
        }
        pageNum = 0;
        getMoreData();
    }

    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }


    private void fetchSinglePost(Long postId, String type) {
        Api.initGetSinglePost(headers, postId, type);
        Api.getSinglePost.clone().enqueue(new Callback<HomePagePost>() {
            @Override
            public void onResponse(Call<HomePagePost> call, Response<HomePagePost> response) {
                if (response.isSuccessful()) {
                    singlePost = response.body();
                    arrayList.add(new Comment(singlePost));
                }
                recyclerAdapter.notifyItemRangeChanged(0, 1);
                getMoreData();
            }

            @Override
            public void onFailure(Call<HomePagePost> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
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
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}
